package org.example.nidabutik.repository;

import org.example.nidabutik.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
    Optional<Payment> findByOrder_Id(Long orderId);
    Optional<Payment> findByTransactionCodeIgnoreCase(String transactionCode);
    List<Payment> findByStatus_CodeIgnoreCase(String code);
    boolean existsByTransactionCodeIgnoreCase(String transactionCode);
}
