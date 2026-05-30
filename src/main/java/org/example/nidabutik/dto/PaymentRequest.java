package org.example.nidabutik.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.example.nidabutik.entity.PaymentMethod;

public record PaymentRequest(
        @NotNull Long orderId,
        @NotNull PaymentMethod method,
        @NotBlank @Size(min = 6, max = 80) String transactionCode
) {
}
