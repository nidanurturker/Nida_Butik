package org.example.nidabutik.service;

import org.example.nidabutik.dto.OrderRequest;
import org.example.nidabutik.dto.OrderResponse;
import org.example.nidabutik.entity.CustomerOrder;
import org.example.nidabutik.entity.OrderItem;
import org.example.nidabutik.entity.Product;
import org.example.nidabutik.exception.BusinessRuleException;
import org.example.nidabutik.exception.ResourceNotFoundException;
import org.example.nidabutik.repository.OrderRepository;
import org.example.nidabutik.repository.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional(readOnly = true)
public class OrderService {
    private final OrderRepository orderRepository;
    private final CustomerService customerService;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, CustomerService customerService, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.customerService = customerService;
        this.productRepository = productRepository;
    }

    public List<OrderResponse> getAllOrders() {
        return orderRepository.findAll().stream().map(this::toResponse).toList();
    }

    public OrderResponse getOrder(Long id) {
        return toResponse(findOrder(id));
    }

    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customerService.findCustomer(request.customerId()));
        BigDecimal total = BigDecimal.ZERO;
        for (var itemRequest : request.items()) {
            Product product = productRepository.findById(itemRequest.productId()).orElseThrow(() -> new ResourceNotFoundException("Urun bulunamadi: " + itemRequest.productId()));
            if (product.getStockQuantity() < itemRequest.quantity()) {
                throw new BusinessRuleException(product.getName() + " icin yeterli stok yok.");
            }
            product.setStockQuantity(product.getStockQuantity() - itemRequest.quantity());
            OrderItem item = new OrderItem();
            item.setProduct(product);
            item.setQuantity(itemRequest.quantity());
            item.setUnitPrice(product.getPrice());
            order.addItem(item);
            total = total.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.quantity())));
        }
        order.setTotalAmount(total);
        return toResponse(orderRepository.save(order));
    }

    public CustomerOrder findOrder(Long id) {
        return orderRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Siparis bulunamadi: " + id));
    }

    OrderResponse toResponse(CustomerOrder order) {
        return new OrderResponse(order.getId(), order.getCustomer().getEmail(), order.getStatus(), order.getTotalAmount(), order.getCreatedAt());
    }
}
