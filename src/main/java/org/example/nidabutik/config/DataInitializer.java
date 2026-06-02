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
import org.example.nidabutik.repository.GenderRepository;
import org.example.nidabutik.repository.OrderRepository;
import org.example.nidabutik.repository.OrderStatusRepository;
import org.example.nidabutik.repository.PaymentMethodRepository;
import org.example.nidabutik.repository.PaymentRepository;
import org.example.nidabutik.repository.PaymentStatusRepository;
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
    private final GenderRepository genderRepository;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    public DataInitializer(CategoryRepository categoryRepository, BrandRepository brandRepository, SupplierRepository supplierRepository, ProductRepository productRepository, CustomerRepository customerRepository, GenderRepository genderRepository, OrderRepository orderRepository, PaymentRepository paymentRepository, OrderStatusRepository orderStatusRepository, PaymentMethodRepository paymentMethodRepository, PaymentStatusRepository paymentStatusRepository) {
        this.categoryRepository = categoryRepository;
        this.brandRepository = brandRepository;
        this.supplierRepository = supplierRepository;
        this.productRepository = productRepository;
        this.customerRepository = customerRepository;
        this.genderRepository = genderRepository;
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentStatusRepository = paymentStatusRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        retireJewelryDemoProducts();

        Category dress = category("Elbise", "Gunluk ve ozel koleksiyon elbiseleri");
        Category blazer = category("Blazer", "Modern kesimli ceket ve blazer modelleri");
        Category knitwear = category("Triko", "Sezonluk triko ve ince orgu parcalari");
        Category top = category("Ust Giyim", "Tisort, bluz ve askili ust modelleri");

        Brand zara = brand("Zara Studio", "Ispanya");
        Brand atelier = brand("Studio Line", "Turkiye");
        Brand edition = brand("Limited Edition", "Italya");

        Supplier supplier = supplier();

        gender("FEMALE", "Kadin");
        gender("MALE", "Erkek");
        gender("OTHER", "Diger");
        orderStatus("CREATED", "Olusturuldu");
        orderStatus("PAID", "Odendi");
        orderStatus("CANCELLED", "Iptal Edildi");
        paymentMethod("CREDIT_CARD", "Kredi Karti");
        paymentMethod("BANK_TRANSFER", "Banka Transferi");
        paymentMethod("CASH_ON_DELIVERY", "Kapida Odeme");
        paymentStatus("PAID", "Odendi");
        paymentStatus("PENDING", "Beklemede");
        paymentStatus("FAILED", "Basarisiz");
        paymentStatus("REFUNDED", "Iade Edildi");

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
        upsertProduct("Asimetrik Kapanmali Triko Top", "ZR-1011", "yumusak triko", new BigDecimal("1899.90"), 17, "/images/zara/zara%20kolsuz%20ti%C5%9F%C3%B6rt.jpg", top, zara, supplier);
        upsertProduct("Asimetrik Kesim Midi Elbise", "ZR-1012", "akiskan kumas", new BigDecimal("2799.90"), 13, "/images/zara/kad%C4%B1n%20a%C3%A7%C4%B1l%C4%B1%C5%9F.jpg", dress, atelier, supplier);
        upsertProduct("Drapeli Saten Midi Elbise", "ZR-1013", "dokumlu saten", new BigDecimal("3199.90"), 11, "/images/zara/kad%C4%B1n%20a%C3%A7%C4%B1l%C4%B1%C5%9F%202.jpg", dress, edition, supplier);
        upsertProduct("Minimal Gunluk Bluz", "ZR-1014", "ince pamuk", new BigDecimal("1299.90"), 30, "/images/zara/resim1.jpg", top, atelier, supplier);
        upsertProduct("Modern Blazer Look", "ZR-1015", "yapili kumas", new BigDecimal("3599.90"), 9, "/images/zara/resim2.jpg", blazer, zara, supplier);

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

    private Gender gender(String code, String label) {
        return genderRepository.findByCodeIgnoreCase(code).orElseGet(() -> genderRepository.save(new Gender(code, label)));
    }

    private OrderStatus orderStatus(String code, String label) {
        return orderStatusRepository.findByCodeIgnoreCase(code).orElseGet(() -> orderStatusRepository.save(new OrderStatus(code, label)));
    }

    private PaymentMethod paymentMethod(String code, String label) {
        return paymentMethodRepository.findByCodeIgnoreCase(code).orElseGet(() -> paymentMethodRepository.save(new PaymentMethod(code, label)));
    }

    private PaymentStatus paymentStatus(String code, String label) {
        return paymentStatusRepository.findByCodeIgnoreCase(code).orElseGet(() -> paymentStatusRepository.save(new PaymentStatus(code, label)));
    }

    private void retireJewelryDemoProducts() {
        List<String> oldModels = List.of("MOON-24", "PEARL-11", "BLUE-08", "CHAIN-03", "EMERALD-14", "TWIST-19", "DAILY-07", "ROSE-22", "SET-30", "SEA-05");
        oldModels.forEach(model -> productRepository.findByModelIgnoreCase(model).ifPresent(productRepository::delete));
    }

    private void seedCustomers() {
        Gender female = gender("FEMALE", "Kadin");
        Gender male = gender("MALE", "Erkek");
        Gender other = gender("OTHER", "Diger");
        ensureCustomer("Zeynep", "Yilmaz", "zeynep@example.com", "+905551112233", female, "Moda Caddesi No:12", "Istanbul");
        ensureCustomer("Elif", "Demir", "elif@example.com", "+905551112244", female, "Bagdat Caddesi No:8", "Istanbul");
        ensureCustomer("Aylin", "Kaya", "aylin@example.com", "+905551112255", female, "Caddebostan Sokak No:4", "Istanbul");
        ensureCustomer("Mert", "Yildiz", "mert@example.com", "+905551112266", male, "Nilufer Bulvari No:21", "Bursa");
        ensureCustomer("Can", "Acar", "can@example.com", "+905551112277", male, "Ataturk Bulvari No:19", "Ankara");
        ensureCustomer("Defne", "Sahin", "defne@example.com", "+905551112288", other, "Tevikiye Mah. No:2", "Izmir");
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
        order.setStatus(orderStatus("PAID", "Odendi"));
        CustomerOrder savedOrder = orderRepository.save(order);

        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setMethod(paymentMethod("CREDIT_CARD", "Kredi Karti"));
        payment.setStatus(paymentStatus("PAID", "Odendi"));
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
        order.setStatus(orderStatus("CREATED", "Olusturuldu"));
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
