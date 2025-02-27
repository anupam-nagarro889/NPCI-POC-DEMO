package com.npci.integration.dto;

import com.npci.integration.models.Transactions;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionDTO {
    private String transactionId;
    private String fromMerchant;
    private String toMerchant;
    private BigDecimal amount;
    private String currency;
    private String status;
    private String referenceId;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
    private String failureReason;

}

