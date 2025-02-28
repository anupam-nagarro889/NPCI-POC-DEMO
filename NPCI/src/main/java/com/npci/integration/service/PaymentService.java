package com.npci.integration.service;

import com.npci.integration.models.PaymentGateway;
import com.npci.integration.models.TransactionStatus;
import com.npci.integration.models.Transactions;
import com.npci.integration.repository.TransactionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class PaymentService {

    @Autowired
    TransactionRepository transactionRepository;

    @Autowired
    RestTemplate restTemplate;

    public Boolean processPayment(Transactions transaction) {

        // Select best gateway (Mock logic: choose any available gateway)
   /*     Optional<PaymentGateway> gatewayOpt = paymentGatewayRepository.findAll().stream().findFirst();
        if (gatewayOpt.isEmpty()) {
            failTransaction(transaction, "No available payment gateway.");
            return;
        }

        PaymentGateway gateway = gatewayOpt.get();*/
        transaction.setStatus(TransactionStatus.PROCESSING);
        log.info("Updating transaction status to processing");
        transactionRepository.save(transaction);

        // Store log entry
//        storeTransactionLog(transaction, LogStatus.FORWARDED, "Payment sent to gateway: " + gateway.getGatewayName());

        // Call payment gateway API
//        boolean paymentSuccess = callPaymentGateway(transaction, gateway);

        /*if (true) {
            completeTransaction(transaction);
        } else {
            failTransaction(transaction, "Payment gateway processing failed.");
        }*/
        return true;
    }

    private boolean callPaymentGateway (Transactions transaction, PaymentGateway gateway){
        try {
            String gatewayUrl = gateway.getApiUrl() + "/processPayment";
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("transactionId", transaction.getTransactionId());
            requestBody.put("amount", transaction.getAmount());
            requestBody.put("currency", transaction.getCurrency());

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

            log.info("Calling payment gateway");
            ResponseEntity<Map> response = restTemplate.exchange(
                    gatewayUrl, HttpMethod.POST, requestEntity, Map.class
            );

            return response.getStatusCode() == HttpStatus.OK && "SUCCESS".equals(response.getBody().get("status"));
        } catch (Exception e) {
            return false;
        }
    }

}
