package org.example.nidabutik.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

public record PaymentRequest(
        @NotNull @Positive Long orderId,
        @NotBlank @Size(max = 30) String method,
        @NotBlank @Size(min = 6, max = 80) String transactionCode
) {
}
