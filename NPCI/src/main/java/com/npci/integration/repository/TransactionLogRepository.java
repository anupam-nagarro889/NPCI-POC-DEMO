package com.npci.integration.repository;


import com.npci.integration.models.TransactionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionLogRepository extends JpaRepository<TransactionLog, Long> {
}
