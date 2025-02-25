package com.npci.integration.service;

import com.npci.integration.dto.TransactionDTO;
import com.npci.integration.exception.ResourceNotFoundException;
import com.npci.integration.models.*;
import com.npci.integration.repository.MerchantRepository;
import com.npci.integration.repository.PaymentGatewayRepository;
import com.npci.integration.repository.TransactionLogRepository;
import com.npci.integration.repository.TransactionRepository;
import org.apache.coyote.BadRequestException;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Service
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final MerchantRepository merchantRepository;
    private final PaymentGatewayRepository paymentGatewayRepository;
    private final RestTemplate restTemplate;
    private final TransactionLogRepository transactionLogRepository;


    public TransactionService(TransactionRepository transactionRepository, MerchantRepository merchantRepository, PaymentGatewayRepository paymentGatewayRepository, RestTemplate restTemplate, TransactionLogRepository transactionLogRepository) {
        this.transactionRepository = transactionRepository;
        this.merchantRepository = merchantRepository;
        this.paymentGatewayRepository = paymentGatewayRepository;
        this.restTemplate = restTemplate;
        this.transactionLogRepository = transactionLogRepository;
    }

    public String initiateTransaction(TransactionDTO transactionDTO) throws BadRequestException {
        // Validate request data
        if (transactionDTO.getAmount() == null || transactionDTO.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new BadRequestException("Please enter a valid amount");
        }

        if (transactionDTO.getFromMerchantCode().equals(transactionDTO.getToMerchantCode())) {
            throw new BadRequestException("Merchant code not allowed.");
        }

        Merchant fromMerchant = merchantRepository.findByMerchantCode(transactionDTO.getFromMerchantCode())
                .orElseThrow(() -> new ResourceNotFoundException("Sender merchant not found"));

        Merchant toMerchant = merchantRepository.findByMerchantCode(transactionDTO.getToMerchantCode())
                .orElseThrow(() -> new ResourceNotFoundException("Receiver merchant not found"));


        // Generate unique transaction ID
        String transactionId = UUID.randomUUID().toString();

        // Create transaction entity
        Transactions transaction = Transactions.builder()
                .transactionId(transactionId)
                .fromMerchant(fromMerchant)
                .toMerchant(toMerchant)
                .amount(transactionDTO.getAmount())
                .currency(transactionDTO.getCurrency())
                .status(TransactionStatus.PENDING)
                .referenceId(transactionDTO.getReferenceId())
                .initiatedAt(LocalDateTime.now())
                .build();

        transactionRepository.save(transaction);
        processPayment(transaction);
        // Return success response
        return transaction.getTransactionId();
    }

    private void processPayment(Transactions transaction) {

        // Select best gateway (Mock logic: choose any available gateway)
   /*     Optional<PaymentGateway> gatewayOpt = paymentGatewayRepository.findAll().stream().findFirst();
        if (gatewayOpt.isEmpty()) {
            failTransaction(transaction, "No available payment gateway.");
            return;
        }

        PaymentGateway gateway = gatewayOpt.get();*/
        transaction.setStatus(TransactionStatus.PROCESSING);
        transactionRepository.save(transaction);

        // Store log entry
//        storeTransactionLog(transaction, LogStatus.FORWARDED, "Payment sent to gateway: " + gateway.getGatewayName());

        // Call payment gateway API
//        boolean paymentSuccess = callPaymentGateway(transaction, gateway);

        if (true) {
            completeTransaction(transaction);
        } else {
            failTransaction(transaction, "Payment gateway processing failed.");
        }
    }

    private boolean callPaymentGateway(Transactions transaction, PaymentGateway gateway) {
        try {
            String gatewayUrl = gateway.getApiUrl() + "/processPayment";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("transactionId", transaction.getTransactionId());
            requestBody.put("amount", transaction.getAmount());
            requestBody.put("currency", transaction.getCurrency());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<Map> response = restTemplate.exchange(
                    gatewayUrl, HttpMethod.POST, requestEntity, Map.class
            );

            return response.getStatusCode() == HttpStatus.OK && "SUCCESS".equals(response.getBody().get("status"));
        } catch (Exception e) {
            return false;
        }
    }

    private void completeTransaction(Transactions transaction) {
        transaction.setStatus(TransactionStatus.SUCCESS);
        transaction.setCompletedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Store log entry
        storeTransactionLog(transaction, LogStatus.COMPLETED, "Transaction completed successfully.");

        // Notify both merchants
        notifyMerchant(transaction.getFromMerchant(), transaction, "Transaction successful.");
        notifyMerchant(transaction.getToMerchant(), transaction, "Transaction successful.");
    }

    private void failTransaction(Transactions transaction, String reason) {
        transaction.setStatus(TransactionStatus.FAILED);
        transaction.setFailureReason(reason);
        transaction.setCompletedAt(LocalDateTime.now());
        transactionRepository.save(transaction);

        // Store log entry
        storeTransactionLog(transaction, LogStatus.FAILED, "Transaction failed: " + reason);

        // Notify both merchants
        notifyMerchant(transaction.getFromMerchant(), transaction, "Transaction failed: " + reason);
        notifyMerchant(transaction.getToMerchant(), transaction, "Transaction failed: " + reason);
    }

    private void storeTransactionLog(Transactions transaction, LogStatus status, String message) {
        TransactionLog log = TransactionLog.builder()
                .transaction(transaction)
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
        transactionLogRepository.save(log);
    }

    private void notifyMerchant(Merchant merchant, Transactions transaction, String message) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("transactionId", transaction.getTransactionId());
            payload.put("status", transaction.getStatus().toString());
            payload.put("message", message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
            System.out.println("Success to notify merchant: " + merchant.getMerchantCode());

//            restTemplate.exchange(merchant.getCallbackUrl(), HttpMethod.POST, requestEntity, Void.class);
        } catch (Exception e) {
            System.out.println("Failed to notify merchant: " + merchant.getMerchantCode());
        }
    }
}
