package org.example.nidabutik.repository;

import org.example.nidabutik.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Long> {
    Optional<PaymentStatus> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);
}
