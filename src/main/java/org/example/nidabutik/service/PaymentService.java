package org.example.nidabutik.service;

import org.example.nidabutik.dto.PaymentRequest;
import org.example.nidabutik.dto.PaymentResponse;
import org.example.nidabutik.entity.CustomerOrder;
import org.example.nidabutik.entity.OrderStatus;
import org.example.nidabutik.entity.Payment;
import org.example.nidabutik.entity.PaymentStatus;
import org.example.nidabutik.exception.BusinessRuleException;
import org.example.nidabutik.repository.OrderRepository;
import org.example.nidabutik.repository.PaymentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService, OrderRepository orderRepository) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse pay(PaymentRequest request) {
        CustomerOrder order = orderService.findOrder(request.orderId());
        if (order.getStatus() == OrderStatus.PAID) {
            throw new BusinessRuleException("Bu siparis zaten odendi.");
        }
        if (paymentRepository.existsByTransactionCodeIgnoreCase(request.transactionCode())) {
            throw new BusinessRuleException("Transaction kodu daha once kullanilmis.");
        }
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(request.method());
        payment.setStatus(PaymentStatus.PAID);
        payment.setAmount(order.getTotalAmount());
        payment.setTransactionCode(request.transactionCode());
        order.setStatus(OrderStatus.PAID);
        orderRepository.save(order);
        return toResponse(paymentRepository.save(payment));
    }

    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse rollbackDemo(PaymentRequest request) {
        PaymentResponse response = pay(request);
        throw new BusinessRuleException("Rollback testi: odeme bilerek iptal edildi.");
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getOrder().getId(), payment.getStatus(), payment.getAmount(), payment.getTransactionCode());
    }
}
