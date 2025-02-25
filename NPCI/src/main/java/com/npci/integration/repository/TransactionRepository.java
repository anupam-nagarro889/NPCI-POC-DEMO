package com.npci.integration.repository;


import com.npci.integration.models.Transactions;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {
    Optional<Transactions> findByTransactionId(String transactionId);
}

