package org.example.nidabutik.dto;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Size(max = 80) String model,
        @NotBlank @Size(max = 40) String material,
        @NotNull @DecimalMin("0.01") BigDecimal price,
        @NotNull @Min(0) Integer stockQuantity,
        @NotBlank @Size(max = 500) String imageUrl,
        @NotNull Long categoryId,
        @NotNull Long brandId,
        @NotNull Long supplierId
) {
}
