package com.npci.integration.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private String transactionId;
    private String fromMerchantCode;
    private String toMerchantCode;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String referenceId;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
    private String failureReason;
}

