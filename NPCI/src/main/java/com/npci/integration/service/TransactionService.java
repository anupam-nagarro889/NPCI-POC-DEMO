package com.npci.integration.service;


import com.npci.integration.dto.TransactionDTO;
import com.npci.integration.exception.BadRequestException;
import com.npci.integration.exception.ResourceNotFoundException;
import com.npci.integration.mapper.TransactionMapper;
import com.npci.integration.models.*;
import com.npci.integration.repository.MerchantRepository;
import com.npci.integration.repository.PaymentGatewayRepository;
import com.npci.integration.repository.TransactionLogRepository;
import com.npci.integration.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MerchantRepository merchantRepository;
    private final TransactionLogRepository transactionLogRepository;
    private final PaymentService paymentService;
    private final NotificationService notificationService;
    private final RedisService redisService;
    private final TransactionMapper transactionMapper;

    public TransactionService(TransactionRepository transactionRepository, MerchantRepository merchantRepository, PaymentGatewayRepository paymentGatewayRepository, RestTemplate restTemplate, TransactionLogRepository transactionLogRepository, PaymentService paymentService, NotificationService notificationService, RedisService redisService, TransactionMapper transactionMapper) {
        this.transactionRepository = transactionRepository;
        this.merchantRepository = merchantRepository;
        this.transactionLogRepository = transactionLogRepository;
        this.paymentService = paymentService;
        this.notificationService = notificationService;
        this.redisService = redisService;
        this.transactionMapper = transactionMapper;
    }

    public Map<String, String> initiateTransaction (TransactionDTO transactionDTO) throws BadRequestException {
        // Validate request data
        log.info("Initiating a transaction");
        if (transactionDTO.getAmount() == null || transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Please enter a valid amount");
        }

        if (transactionDTO.getFromMerchant().equals(transactionDTO.getToMerchant())) {
            throw new BadRequestException("Merchant code not allowed.");
        }

        Merchant fromMerchant = merchantRepository.findByMerchantCode(transactionDTO.getFromMerchant())
                .orElseThrow(() -> new ResourceNotFoundException("Sender merchant not found"));

        Merchant toMerchant = merchantRepository.findByMerchantCode(transactionDTO.getToMerchant())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver merchant not found"));

        // Generate unique transaction ID
        log.info("Generating a transaction ID");
        String transactionId = UUID.randomUUID().toString();

        // Create transaction entity
        Transactions transaction = createTransactionEntity(transactionId, fromMerchant, toMerchant, transactionDTO);
        transactionRepository.save(transaction);

        boolean isPaymentCompleted = paymentService.processPayment(transaction);

        if (isPaymentCompleted) {
            log.info("Transaction completed.");
            completeTransaction(transaction);
        } else {
            log.info("Transaction Failed.");
            failTransaction(transaction, "Payment gateway processing failed.");
        }
        log.info("Creating transaction cache.");
        redisService.set(transaction.getTransactionId(), transaction, 500L);

        Map<String, String> response = new HashMap<>();
        response.put("Message", "Transaction successfully completed...");
        response.put("Transaction id: ", transactionId);
        return response;
    }

    private Transactions createTransactionEntity (String transactionId, Merchant fromMerchant, Merchant
            toMerchant, TransactionDTO transactionDTO){
        Transactions transactions = Transactions.builder()
                .transactionId(transactionId)
                .fromMerchant(fromMerchant)
                .toMerchant(toMerchant)
                .amount(transactionDTO.getAmount())
                .currency(transactionDTO.getCurrency())
                .status(TransactionStatus.PENDING)
                .referenceId(transactionDTO.getReferenceId())
                .initiatedAt(LocalDateTime.now())
                .build();
        return transactions;
    }

    private void completeTransaction (Transactions transaction){
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCompletedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Store log entry
        storeTransactionLog(transaction, LogStatus.COMPLETED, "Transaction completed successfully.");

        // Notify both merchants
        notificationService.notifyMerchant(transaction.getFromMerchant(), transaction, "Transaction successful.");
        notificationService.notifyMerchant(transaction.getToMerchant(), transaction, "Transaction successful.");
    }

    private void failTransaction (Transactions transaction, String reason){
        transaction.setStatus(TransactionStatus.FAILED);
        transaction.setFailureReason(reason);
        transaction.setCompletedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Store log entry
        storeTransactionLog(transaction, LogStatus.FAILED, "Transaction failed: " + reason);

        // Notify both merchants
        notificationService.notifyMerchant(transaction.getFromMerchant(), transaction, "Transaction failed: " + reason);
        notificationService.notifyMerchant(transaction.getToMerchant(), transaction, "Transaction failed: " + reason);
    }

    private void storeTransactionLog (Transactions transaction, LogStatus status, String message){
        log.info("Storing transaction audit logs.");
        TransactionLog log = TransactionLog.builder()
                .transaction(transaction)
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        transactionLogRepository.save(log);
    }

    public TransactionDTO getTransaction(String transactionId) {
        Transactions transactions = redisService.get(transactionId, Transactions.class);
        if(transactions != null){
            log.info("Fetching from redis.");
            return transactionMapper.transactionToTransactionDto(transactions);
        }else{
            log.info("Fetching from Database...");
            Transactions transaction = transactionRepository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new RuntimeException("Transaction not found"));
            redisService.set(transactionId, transaction, 500L);
            return transactionMapper.transactionToTransactionDto(transaction);
        }

    }

    public List<TransactionDTO> getAllTransactions() {
        log.info("Getting all transaction");
        List<Transactions> transactionList = transactionRepository.findAll();
        return transactionMapper.transactionToDtoList(transactionList);
    }

    public void deleteTransaction(String transactionId) {
        Transactions transaction = transactionRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new ResourceNotFoundException("Transaction not found"));
        redisService.delete(transactionId);
        transaction.setStatus(TransactionStatus.DELETED);
        log.info("Deleting transaction");
        transactionRepository.save(transaction);
    }


}
