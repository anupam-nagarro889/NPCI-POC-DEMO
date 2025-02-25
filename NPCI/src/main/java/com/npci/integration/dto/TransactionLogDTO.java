package com.npci.integration.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionLogDTO {
    private Long id;
    private String transactionId;
    private String status;
    private String message;
    private LocalDateTime timestamp;
}
