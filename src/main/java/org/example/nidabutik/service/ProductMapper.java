package org.example.nidabutik.service;

import org.example.nidabutik.dto.ProductResponse;
import org.example.nidabutik.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getModel(),
                product.getMaterial(),
                product.getPrice(),
                product.getStockQuantity(),
                product.getImageUrl(),
                product.getCategory().getName(),
                product.getBrand().getName(),
                product.getSupplier().getName()
        );
    }
}
