package org.example.nidabutik.repository;

import org.example.nidabutik.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);
}
