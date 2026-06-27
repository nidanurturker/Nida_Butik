package org.example.nidabutik.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 80) String model,
        @NotBlank @Size(max = 40) String material,
        @NotBlank @Size(max = 40) String size,
        @NotNull @Positive BigDecimal price,
        @NotNull @PositiveOrZero Integer stockQuantity,
        @NotBlank @Size(max = 500) String imageUrl,
        @NotNull @Positive Long categoryId,
        @NotNull @Positive Long brandId,
        @NotNull @Positive Long supplierId
) {
}
