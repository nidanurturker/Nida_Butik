package org.example.nidabutik.repository;

import org.example.nidabutik.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findByModelIgnoreCase(String model);
    boolean existsByModelIgnoreCase(String model);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findByNameContainingIgnoreCase(String name);
    List<Product> findByModelContainingIgnoreCase(String model);
    List<Product> findByBrand_NameIgnoreCase(String brandName);
    List<Product> findByCategory_NameIgnoreCase(String categoryName);
    List<Product> findByPriceBetweenAndModelContainingIgnoreCaseAndBrand_NameIgnoreCase(BigDecimal minPrice, BigDecimal maxPrice, String model, String brandName);
    List<Product> findTop8ByOrderByPriceDesc();
}
