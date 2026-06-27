package org.example.nidabutik.dto;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String model,
        String material,
        String size,
        BigDecimal price,
        Integer stockQuantity,
        String imageUrl,
        String category,
        String brand,
        String supplier
) {
}
