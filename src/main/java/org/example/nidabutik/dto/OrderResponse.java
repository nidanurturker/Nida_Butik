package org.example.nidabutik.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record OrderResponse(
        Long id,
        String customerEmail,
        String statusCode,
        String statusLabel,
        BigDecimal totalAmount,
        LocalDateTime createdAt
) {
}
