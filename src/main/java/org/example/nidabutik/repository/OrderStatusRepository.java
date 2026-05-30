package org.example.nidabutik.repository;

import org.example.nidabutik.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface OrderStatusRepository extends JpaRepository<OrderStatus, Long> {
    Optional<OrderStatus> findByCodeIgnoreCase(String code);
    boolean existsByCodeIgnoreCase(String code);
}
