package com.npci.integration.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "transactions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transactions implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String transactionId;

    @ManyToOne
    @JoinColumn(name = "from_merchant_id", nullable = false)
    private Merchant fromMerchant;

    @ManyToOne
    @JoinColumn(name = "to_merchant_id", nullable = false)
    private Merchant toMerchant;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, length = 10)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status = TransactionStatus.PENDING;

    @Column(nullable = false, unique = true)
    private String referenceId;

    @Column(nullable = false, updatable = false)
    private LocalDateTime initiatedAt = LocalDateTime.now();

    private LocalDateTime completedAt;

    private String failureReason;
}

