package org.example.nidabutik.service;

import org.example.nidabutik.dto.ProductRequest;
import org.example.nidabutik.dto.ProductResponse;
import org.example.nidabutik.entity.Product;
import org.example.nidabutik.exception.ResourceNotFoundException;
import org.example.nidabutik.repository.BrandRepository;
import org.example.nidabutik.repository.CategoryRepository;
import org.example.nidabutik.repository.ProductRepository;
import org.example.nidabutik.repository.SupplierRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final SupplierRepository supplierRepository;
    private final ProductMapper productMapper;

    public ProductService(ProductRepository productRepository, CategoryRepository categoryRepository, BrandRepository brandRepository, SupplierRepository supplierRepository, ProductMapper productMapper) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.supplierRepository = supplierRepository;
        this.productMapper = productMapper;
    }

    public List<ProductResponse> getAllProducts() {
        return productRepository.findAll().stream().map(productMapper::toResponse).toList();
    }

    public ProductResponse getProduct(Long id) {
        return productMapper.toResponse(findProduct(id));
    }

    public List<ProductResponse> filterProducts(BigDecimal minPrice, BigDecimal maxPrice, String model, String brand) {
        if (model != null && brand != null) {
            return productRepository.findByPriceBetweenAndModelContainingIgnoreCaseAndBrand_NameIgnoreCase(minPrice, maxPrice, model, brand).stream().map(productMapper::toResponse).toList();
        }
        if (model != null) {
            List<Product> modelMatches = productRepository.findByModelContainingIgnoreCase(model);
            List<Product> nameMatches = productRepository.findByNameContainingIgnoreCase(model);
            return java.util.stream.Stream.concat(modelMatches.stream(), nameMatches.stream())
                    .distinct()
                    .filter(product -> product.getPrice().compareTo(minPrice) >= 0 && product.getPrice().compareTo(maxPrice) <= 0)
                    .map(productMapper::toResponse)
                    .toList();
        }
        if (brand != null) {
            return productRepository.findByBrand_NameIgnoreCase(brand).stream()
                    .filter(product -> product.getPrice().compareTo(minPrice) >= 0 && product.getPrice().compareTo(maxPrice) <= 0)
                    .map(productMapper::toResponse)
                    .toList();
        }
        return productRepository.findByPriceBetween(minPrice, maxPrice).stream().map(productMapper::toResponse).toList();
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Product product = new Product();
        applyRequest(product, request);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = findProduct(id);
        applyRequest(product, request);
        return productMapper.toResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        productRepository.delete(findProduct(id));
    }

    Product findProduct(Long id) {
        return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Urun bulunamadi: " + id));
    }

    private void applyRequest(Product product, ProductRequest request) {
        product.setName(request.name());
        product.setModel(request.model());
        product.setMaterial(request.material());
        product.setPrice(request.price());
        product.setStockQuantity(request.stockQuantity());
        product.setImageUrl(request.imageUrl());
        product.setCategory(categoryRepository.findById(request.categoryId()).orElseThrow(() -> new ResourceNotFoundException("Kategori bulunamadi: " + request.categoryId())));
        product.setBrand(brandRepository.findById(request.brandId()).orElseThrow(() -> new ResourceNotFoundException("Marka bulunamadi: " + request.brandId())));
        product.setSupplier(supplierRepository.findById(request.supplierId()).orElseThrow(() -> new ResourceNotFoundException("Tedarikci bulunamadi: " + request.supplierId())));
    }
}
