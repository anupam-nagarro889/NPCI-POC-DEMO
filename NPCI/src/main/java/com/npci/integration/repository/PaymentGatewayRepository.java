package com.npci.integration.repository;


import com.npci.integration.models.PaymentGateway;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentGatewayRepository extends JpaRepository<PaymentGateway, Long> {
}
