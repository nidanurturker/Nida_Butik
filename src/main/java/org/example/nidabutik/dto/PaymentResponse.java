package org.example.nidabutik.dto;

import org.example.nidabutik.entity.PaymentStatus;

import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        Long orderId,
        PaymentStatus status,
        BigDecimal amount,
        String transactionCode
) {
}
