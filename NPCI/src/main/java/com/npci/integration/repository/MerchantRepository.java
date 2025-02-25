package com.npci.integration.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.npci.integration.models.Merchant;

import java.util.Optional;

@Repository
public interface MerchantRepository extends JpaRepository<Merchant, Long> {

    Optional<Merchant> findByMerchantCode(String fromMerchantCode);
}
