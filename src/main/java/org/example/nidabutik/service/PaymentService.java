package org.example.nidabutik.service;

import org.example.nidabutik.dto.PaymentRequest;
import org.example.nidabutik.dto.PaymentResponse;
import org.example.nidabutik.entity.CustomerOrder;
import org.example.nidabutik.entity.Payment;
import org.example.nidabutik.exception.BusinessRuleException;
import org.example.nidabutik.repository.OrderRepository;
import org.example.nidabutik.repository.OrderStatusRepository;
import org.example.nidabutik.repository.PaymentMethodRepository;
import org.example.nidabutik.repository.PaymentRepository;
import org.example.nidabutik.repository.PaymentStatusRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class PaymentService {
    private final PaymentRepository paymentRepository;
    private final OrderService orderService;
    private final OrderRepository orderRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final OrderStatusRepository orderStatusRepository;

    public PaymentService(PaymentRepository paymentRepository, OrderService orderService, OrderRepository orderRepository, PaymentMethodRepository paymentMethodRepository, PaymentStatusRepository paymentStatusRepository, OrderStatusRepository orderStatusRepository) {
        this.paymentRepository = paymentRepository;
        this.orderService = orderService;
        this.orderRepository = orderRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.orderStatusRepository = orderStatusRepository;
    }

    public List<PaymentResponse> getAllPayments() {
        return paymentRepository.findAll().stream().map(this::toResponse).toList();
    }

    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse pay(PaymentRequest request) {
        CustomerOrder order = orderService.findOrder(request.orderId());
        if ("PAID".equalsIgnoreCase(order.getStatus().getCode())) {
            throw new BusinessRuleException("Bu siparis zaten odendi.");
        }
        if (paymentRepository.existsByTransactionCodeIgnoreCase(request.transactionCode())) {
            throw new BusinessRuleException("Transaction kodu daha once kullanilmis.");
        }
        Payment payment = new Payment();
        payment.setOrder(order);
        payment.setMethod(paymentMethodRepository.findByCodeIgnoreCase(request.method())
                .orElseThrow(() -> new BusinessRuleException("Gecersiz odeme yontemi: " + request.method())));
        payment.setStatus(paymentStatusRepository.findByCodeIgnoreCase("PAID")
                .orElseThrow(() -> new BusinessRuleException("Odeme durumu tanimli degil: PAID")));
        payment.setAmount(order.getTotalAmount());
        payment.setTransactionCode(request.transactionCode());
        order.setStatus(orderStatusRepository.findByCodeIgnoreCase("PAID")
                .orElseThrow(() -> new BusinessRuleException("Siparis durumu tanimli degil: PAID")));
        orderRepository.save(order);
        return toResponse(paymentRepository.save(payment));
    }

    @Transactional(rollbackFor = Exception.class)
    public PaymentResponse rollbackDemo(PaymentRequest request) {
        pay(request);
        throw new BusinessRuleException("Rollback testi: odeme bilerek iptal edildi.");
    }

    private PaymentResponse toResponse(Payment payment) {
        return new PaymentResponse(
                payment.getId(),
                payment.getOrder().getId(),
                payment.getStatus().getCode(),
                payment.getStatus().getLabel(),
                payment.getAmount(),
                payment.getTransactionCode()
        );
    }
}
