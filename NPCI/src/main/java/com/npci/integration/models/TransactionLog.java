package com.npci.integration.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "transaction_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionLog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "transaction_id", nullable = false)
    private Transactions transaction;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LogStatus status;

    @Column(nullable = false)
    private String message;

    @Column(nullable = false, updatable = false)
    private LocalDateTime timestamp = LocalDateTime.now();
}
