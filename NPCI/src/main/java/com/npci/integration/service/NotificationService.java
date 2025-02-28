package com.npci.integration.service;

import com.npci.integration.models.Merchant;
import com.npci.integration.models.Transactions;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Service
public class NotificationService {

    public void notifyMerchant(Merchant merchant, Transactions transaction, String message) {
        try {
            Map<String, Object> payload = new HashMap<>();
            payload.put("transactionId", transaction.getTransactionId());
            payload.put("status", transaction.getStatus().toString());
            payload.put("message", message);

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(payload, headers);
            log.info("Notifying merchant: {}", merchant.getMerchantCode());
//            restTemplate.exchange(merchant.getCallbackUrl(), HttpMethod.POST, requestEntity, Void.class);
        } catch (Exception e) {
            log.info("Failed to notify merchant: {} ", merchant.getMerchantCode());
        }
    }



}
