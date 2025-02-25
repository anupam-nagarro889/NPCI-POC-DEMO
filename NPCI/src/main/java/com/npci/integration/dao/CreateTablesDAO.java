package com.npci.integration.dao;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class CreateTablesDAO {
/*
    private final JdbcTemplate jdbcTemplate;

    public CreateTablesDAO(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @PostConstruct
    public void createTables() {
        createMerchantTable();
        createTransactionTable();
        createPaymentGatewayTable();
        createTransactionLogTable();
    }

    public void createMerchantTable() {
        String query = """
            CREATE TABLE IF NOT EXISTS npci_poc.merchants (
                id SERIAL PRIMARY KEY,
                merchant_code VARCHAR(50) UNIQUE NOT NULL,
                name VARCHAR(255) NOT NULL,
                callback_url VARCHAR(500) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;
        jdbcTemplate.update(query);
        System.out.println("Merchant Table Created");
    }

    public void createTransactionTable() {
        String query = """
            CREATE TABLE IF NOT EXISTS npci_poc.transactions (
                id SERIAL PRIMARY KEY,
                transaction_id VARCHAR(100) UNIQUE NOT NULL,
                from_merchant_id BIGINT NOT NULL,
                to_merchant_id BIGINT NOT NULL,
                amount DECIMAL(18,2) NOT NULL,
                currency VARCHAR(10) NOT NULL,
                status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
                reference_id VARCHAR(100) UNIQUE NOT NULL,
                initiated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                completed_at TIMESTAMP NULL,
                failure_reason TEXT NULL,
                FOREIGN KEY (from_merchant_id) REFERENCES merchants(id),
                FOREIGN KEY (to_merchant_id) REFERENCES merchants(id)
            );
        """;
        jdbcTemplate.update(query);
        System.out.println("Transaction Table Created");
    }

    public void createPaymentGatewayTable() {
        String query = """
            CREATE TABLE IF NOT EXISTS npci_poc.payment_gateways (
                id SERIAL PRIMARY KEY,
                gateway_code VARCHAR(50) UNIQUE NOT NULL,
                gateway_name VARCHAR(255) NOT NULL,
                api_url VARCHAR(500) NOT NULL,
                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            );
        """;
        jdbcTemplate.update(query);
        System.out.println("Payment Gateway Table Created");
    }

    public void createTransactionLogTable() {
        String query = """
            CREATE TABLE IF NOT EXISTS npci_poc.transaction_logs (
                id SERIAL PRIMARY KEY,
                transaction_id BIGINT NOT NULL,
                status VARCHAR(20) NOT NULL,
                message TEXT NOT NULL,
                timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (transaction_id) REFERENCES transactions(id)
            );
        """;
        jdbcTemplate.update(query);
        System.out.println("Transaction Log Table Created");
    }*/
}
