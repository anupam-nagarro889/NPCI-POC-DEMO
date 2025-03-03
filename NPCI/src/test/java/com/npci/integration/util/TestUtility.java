package com.npci.integration.util;

import com.npci.integration.dto.MerchantDTO;
import com.npci.integration.dto.TransactionDTO;
import com.npci.integration.models.Merchant;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestUtility {

    public MerchantDTO getMerchantDto(Long id, String code, String name, String callbackUrl){
        MerchantDTO merchantDTO = new MerchantDTO();
        merchantDTO.setId(id);
        merchantDTO.setCallbackUrl(callbackUrl);
        merchantDTO.setMerchantCode(code);
        merchantDTO.setName(name);
        return merchantDTO;
    }

    public Merchant getMerchant(Long id, String code, String name, String callbackUrl){
        Merchant merchant = new Merchant();
        merchant.setId(id);
        merchant.setCallbackUrl(callbackUrl);
        merchant.setMerchantCode(code);
        merchant.setName(name);
        return merchant;
    }

    public TransactionDTO getTransactionDto(String id, String merchantA, String merchantB, String refId) {
        TransactionDTO transactionDTO = new TransactionDTO();
        transactionDTO.setTransactionId(id);
        transactionDTO.setFromMerchant(merchantA);
        transactionDTO.setToMerchant(merchantB);
        transactionDTO.setAmount(BigDecimal.valueOf(1500.75));
        transactionDTO.setCurrency("INR");
        transactionDTO.setStatus("COMPLETED");
        transactionDTO.setReferenceId(refId);
        transactionDTO.setInitiatedAt(LocalDateTime.now().minusMinutes(30));
        transactionDTO.setCompletedAt(LocalDateTime.now());
        transactionDTO.setFailureReason(null);

        return transactionDTO;
    }

}
