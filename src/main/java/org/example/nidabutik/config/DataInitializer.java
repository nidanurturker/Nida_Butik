package org.example.nidabutik.config;

import org.example.nidabutik.entity.Brand;
import org.example.nidabutik.entity.Category;
import org.example.nidabutik.entity.Customer;
import org.example.nidabutik.entity.CustomerOrder;
import org.example.nidabutik.entity.Gender;
import org.example.nidabutik.entity.OrderItem;
import org.example.nidabutik.entity.OrderStatus;
import org.example.nidabutik.entity.Payment;
import org.example.nidabutik.entity.PaymentMethod;
import org.example.nidabutik.entity.PaymentStatus;
import org.example.nidabutik.entity.Product;
import org.example.nidabutik.entity.Supplier;
import org.example.nidabutik.repository.BrandRepository;
import org.example.nidabutik.repository.CategoryRepository;
import org.example.nidabutik.repository.CustomerRepository;
import org.example.nidabutik.repository.OrderRepository;
import org.example.nidabutik.repository.PaymentRepository;
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
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    public DataInitializer(CategoryRepository categoryRepository, BrandRepository brandRepository, SupplierRepository supplierRepository, ProductRepository productRepository, CustomerRepository customerRepository, OrderRepository orderRepository, PaymentRepository paymentRepository) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        retireJewelryDemoProducts();

        Category dress = category("Elbise", "Gunluk ve ozel koleksiyon elbiseleri");
        Category blazer = category("Blazer", "Modern kesimli ceket ve blazer modelleri");
        Category trousers = category("Pantolon", "Yuksek bel ve genis paça pantolonlar");
        Category knitwear = category("Triko", "Sezonluk triko ve ince orgu parcalari");
        Category top = category("Ust Giyim", "Tisort, bluz ve askili ust modelleri");
        Category skirt = category("Etek", "Mini ve midi etek secenekleri");

        Brand zara = brand("Zara Studio", "Ispanya");
        Brand atelier = brand("Studio Line", "Turkiye");
        Brand edition = brand("Limited Edition", "Italya");

        Supplier supplier = supplier();

        upsertProduct("Kemerli Beyaz Midi Elbise", "ZR-1001", "keten karisimli kumas", new BigDecimal("2499.90"), 18, "/images/zara/00387075250-p.jpg", dress, zara, supplier);
        upsertProduct("Kirmizi Straplez Uzun Elbise", "ZR-1002", "dokumlu viskon", new BigDecimal("3299.90"), 12, "/images/zara/01165458632-p.jpg", dress, edition, supplier);
        upsertProduct("Cizgili Ince Triko Tunik", "ZR-1003", "pamuklu triko", new BigDecimal("1699.90"), 22, "/images/zara/02142175500-p.jpg", knitwear, atelier, supplier);
        upsertProduct("Siyah Halter Bluz ve Desenli Sort", "ZR-1004", "saten dokulu kumas", new BigDecimal("1899.90"), 15, "/images/zara/02157023700-p.jpg", top, zara, supplier);
        upsertProduct("Dokulu Ekose Ceket", "ZR-1005", "tuvit dokulu kumas", new BigDecimal("2799.90"), 11, "/images/zara/02719903084-p.jpg", blazer, edition, supplier);
        upsertProduct("Oversize Siyah Blazer", "ZR-1006", "yun karisimli kumas", new BigDecimal("3499.90"), 10, "/images/zara/02753122700-p.jpg", blazer, zara, supplier);
        upsertProduct("Pudra Askili Midi Elbise", "ZR-1007", "akiskan saten", new BigDecimal("2199.90"), 16, "/images/zara/02860342902-p.jpg", dress, atelier, supplier);
        upsertProduct("Kruvaze Dugmeli Mini Elbise", "ZR-1008", "pamuklu kanvas", new BigDecimal("2599.90"), 14, "/images/zara/02930727052-p.jpg", dress, zara, supplier);
        upsertProduct("Basic Beyaz Tisort ve Denim", "ZR-1009", "pamuk denim kombin", new BigDecimal("1499.90"), 28, "/images/zara/04424306250-p.jpg", top, atelier, supplier);
        upsertProduct("Puantiye Desenli Midi Elbise", "ZR-1010", "hafif viskon", new BigDecimal("1999.90"), 19, "/images/zara/05039190300-p.jpg", dress, edition, supplier);

        seedCustomers();
        seedDemoOrdersAndPayments();
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
            supplier.setCity("Istanbul");
            return supplierRepository.save(supplier);
        });
    }

    private void retireJewelryDemoProducts() {
        List<String> oldModels = List.of("MOON-24", "PEARL-11", "BLUE-08", "CHAIN-03", "EMERALD-14", "TWIST-19", "DAILY-07", "ROSE-22", "SET-30", "SEA-05");
        oldModels.forEach(model -> productRepository.findByModelIgnoreCase(model).ifPresent(productRepository::delete));
    }

    private void seedCustomers() {
        ensureCustomer("Zeynep", "Yilmaz", "zeynep@example.com", "+905551112233", Gender.FEMALE, "Moda Caddesi No:12", "Istanbul");
        ensureCustomer("Elif", "Demir", "elif@example.com", "+905551112244", Gender.FEMALE, "Bagdat Caddesi No:8", "Istanbul");
        ensureCustomer("Aylin", "Kaya", "aylin@example.com", "+905551112255", Gender.FEMALE, "Caddebostan Sokak No:4", "Istanbul");
        ensureCustomer("Mert", "Yildiz", "mert@example.com", "+905551112266", Gender.MALE, "Nilufer Bulvari No:21", "Bursa");
        ensureCustomer("Can", "Acar", "can@example.com", "+905551112277", Gender.MALE, "Ataturk Bulvari No:19", "Ankara");
    }

    private void ensureCustomer(String firstName, String lastName, String email, String phone, Gender gender, String address, String city) {
        if (customerRepository.existsByEmailIgnoreCase(email)) {
            return;
        }
        Customer customer = new Customer();
        customer.setFirstName(firstName);
        customer.setLastName(lastName);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setGender(gender);
        customer.setAddress(address);
        customer.setCity(city);
        customerRepository.save(customer);
    }

    private void seedDemoOrdersAndPayments() {
        if (orderRepository.count() > 0) {
            return;
        }

        createPaidOrder("zeynep@example.com", "TXN-ZY-0001", OrderLine.of("ZR-1001", 1), OrderLine.of("ZR-1003", 2));
        createPaidOrder("elif@example.com", "TXN-EL-0002", OrderLine.of("ZR-1005", 1), OrderLine.of("ZR-1009", 1));
        createPaidOrder("aylin@example.com", "TXN-AY-0003", OrderLine.of("ZR-1007", 2));
        createPaidOrder("mert@example.com", "TXN-ME-0004", OrderLine.of("ZR-1004", 2));
        createOpenOrder("can@example.com", OrderLine.of("ZR-1006", 1), OrderLine.of("ZR-1010", 1));
    }

    private void createPaidOrder(String email, String transactionCode, OrderLine... lines) {
        CustomerOrder order = createOrder(email, lines);
        order.setStatus(OrderStatus.PAID);
        CustomerOrder savedOrder = orderRepository.save(order);

        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setMethod(PaymentMethod.CREDIT_CARD);
        payment.setStatus(PaymentStatus.PAID);
        payment.setAmount(savedOrder.getTotalAmount());
        payment.setTransactionCode(transactionCode);
        paymentRepository.save(payment);
    }

    private void createOpenOrder(String email, OrderLine... lines) {
        createOrder(email, lines);
    }

    private CustomerOrder createOrder(String email, OrderLine... lines) {
        Customer customer = customerRepository.findByEmailIgnoreCase(email).orElseThrow();
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer);
        BigDecimal total = BigDecimal.ZERO;
        for (OrderLine line : lines) {
            Product product = productRepository.findByModelIgnoreCase(line.model()).orElseThrow();
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(line.quantity());
            item.setUnitPrice(product.getPrice());
            order.addItem(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(line.quantity())));
        }
        order.setTotalAmount(total);
        return orderRepository.save(order);
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

    private record OrderLine(String model, int quantity) {
        static OrderLine of(String model, int quantity) {
            return new OrderLine(model, quantity);
        }
    }
}
