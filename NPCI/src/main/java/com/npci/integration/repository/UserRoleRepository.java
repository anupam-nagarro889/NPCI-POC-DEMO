package com.npci.integration.repository;

import com.npci.integration.models.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRoleRepository extends JpaRepository<UserRoles, Long> {

    List<UserRoles> findAllByUserId(Long userId);
}
