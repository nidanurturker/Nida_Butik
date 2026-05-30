package org.example.nidabutik.config;

import org.example.nidabutik.entity.Brand;
import org.example.nidabutik.entity.Category;
import org.example.nidabutik.entity.Customer;
import org.example.nidabutik.entity.Gender;
import org.example.nidabutik.entity.Product;
import org.example.nidabutik.entity.Supplier;
import org.example.nidabutik.repository.BrandRepository;
import org.example.nidabutik.repository.CategoryRepository;
import org.example.nidabutik.repository.CustomerRepository;
import org.example.nidabutik.repository.ProductRepository;
import org.example.nidabutik.repository.SupplierRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Component
public class DataInitializer implements CommandLineRunner {
    private final CategoryRepository categoryRepository;
    private final BrandRepository brandRepository;
    private final SupplierRepository supplierRepository;
    private final ProductRepository productRepository;
    private final CustomerRepository customerRepository;

    public DataInitializer(CategoryRepository categoryRepository, BrandRepository brandRepository, SupplierRepository supplierRepository, ProductRepository productRepository, CustomerRepository customerRepository) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        retireJewelryDemoProducts();

        Category dress = category("Elbise", "Günlük ve özel koleksiyon elbiseleri");
        Category blazer = category("Blazer", "Modern kesimli ceket ve blazer modelleri");
        Category trousers = category("Pantolon", "Yüksek bel ve geniş paça pantolonlar");
        Category knitwear = category("Triko", "Sezonluk triko ve ince örgü parçalar");
        Category top = category("Üst Giyim", "Tişört, bluz ve askılı üst modelleri");
        Category skirt = category("Etek", "Mini ve midi etek seçenekleri");

        Brand zara = brand("Zara Studio", "İspanya");
        Brand atelier = brand("Studio Line", "Türkiye");
        Brand edition = brand("Limited Edition", "İtalya");

        Supplier supplier = supplier();

        upsertProduct("Kemerli Beyaz Midi Elbise", "ZR-1001", "keten karışımlı kumaş", new BigDecimal("2499.90"), 18, "/images/zara/00387075250-p.jpg", dress, zara, supplier);
        upsertProduct("Kırmızı Straplez Uzun Elbise", "ZR-1002", "dökümlü viskon", new BigDecimal("3299.90"), 12, "/images/zara/01165458632-p.jpg", dress, edition, supplier);
        upsertProduct("Çizgili İnce Triko Tunik", "ZR-1003", "pamuklu triko", new BigDecimal("1699.90"), 22, "/images/zara/02142175500-p.jpg", knitwear, atelier, supplier);
        upsertProduct("Siyah Halter Bluz ve Desenli Şort", "ZR-1004", "saten dokulu kumaş", new BigDecimal("1899.90"), 15, "/images/zara/02157023700-p.jpg", top, zara, supplier);
        upsertProduct("Dokulu Ekose Ceket", "ZR-1005", "tüvit dokulu kumaş", new BigDecimal("2799.90"), 11, "/images/zara/02719903084-p.jpg", blazer, edition, supplier);
        upsertProduct("Oversize Siyah Blazer", "ZR-1006", "yün karışımlı kumaş", new BigDecimal("3499.90"), 10, "/images/zara/02753122700-p.jpg", blazer, zara, supplier);
        upsertProduct("Pudra Askılı Midi Elbise", "ZR-1007", "akışkan saten", new BigDecimal("2199.90"), 16, "/images/zara/02860342902-p.jpg", dress, atelier, supplier);
        upsertProduct("Kruvaze Düğmeli Mini Elbise", "ZR-1008", "pamuklu kanvas", new BigDecimal("2599.90"), 14, "/images/zara/02930727052-p.jpg", dress, zara, supplier);
        upsertProduct("Basic Beyaz Tişört ve Denim", "ZR-1009", "pamuk denim kombin", new BigDecimal("1499.90"), 28, "/images/zara/04424306250-p.jpg", top, atelier, supplier);
        upsertProduct("Puantiye Desenli Midi Elbise", "ZR-1010", "hafif viskon", new BigDecimal("1999.90"), 19, "/images/zara/05039190300-p.jpg", dress, edition, supplier);

        customer();
    }

    private Category category(String name, String description) {
        return categoryRepository.findByNameIgnoreCase(name).orElseGet(() -> categoryRepository.save(new Category(name, description)));
    }

    private Brand brand(String name, String country) {
        return brandRepository.findByNameIgnoreCase(name).orElseGet(() -> brandRepository.save(new Brand(name, country)));
    }

    private Supplier supplier() {
        return supplierRepository.findByEmailIgnoreCase("tedarik@zarastudio.com").orElseGet(() -> {
            Supplier supplier = new Supplier();
            supplier.setName("Zara Studio Tedarik");
            supplier.setEmail("tedarik@zarastudio.com");
            supplier.setPhone("+902121112233");
            supplier.setCity("İstanbul");
            return supplierRepository.save(supplier);
        });
    }

    private void retireJewelryDemoProducts() {
        List<String> oldModels = List.of("MOON-24", "PEARL-11", "BLUE-08", "CHAIN-03", "EMERALD-14", "TWIST-19", "DAILY-07", "ROSE-22", "SET-30", "SEA-05");
        oldModels.forEach(model -> productRepository.findByModelIgnoreCase(model).ifPresent(productRepository::delete));
    }

    private void customer() {
        if (customerRepository.existsByEmailIgnoreCase("zeynep@example.com")) {
            return;
        }
        Customer customer = new Customer();
        customer.setFirstName("Zeynep");
        customer.setLastName("Yılmaz");
        customer.setEmail("zeynep@example.com");
        customer.setPhone("+905551112233");
        customer.setGender(Gender.FEMALE);
        customer.setAddress("Moda Caddesi No:12");
        customer.setCity("İstanbul");
        customerRepository.save(customer);
    }

    private void upsertProduct(String name, String model, String material, BigDecimal price, int stock, String imageUrl, Category category, Brand brand, Supplier supplier) {
        Product product = productRepository.findByModelIgnoreCase(model).orElseGet(Product::new);
        product.setName(name);
        product.setModel(model);
        product.setMaterial(material);
        product.setPrice(price);
        product.setStockQuantity(stock);
        product.setImageUrl(imageUrl);
        product.setCategory(category);
        product.setBrand(brand);
        product.setSupplier(supplier);
        productRepository.save(product);
    }
}
