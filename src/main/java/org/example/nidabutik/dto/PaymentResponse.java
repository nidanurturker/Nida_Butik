package org.example.nidabutik.dto;

import java.math.BigDecimal;

public record PaymentResponse(
        Long id,
        Long orderId,
        String statusCode,
        String statusLabel,
        BigDecimal amount,
        String transactionCode
) {
}
