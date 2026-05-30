package org.example.nidabutik.controller;

import jakarta.validation.Valid;
import org.example.nidabutik.dto.PaymentRequest;
import org.example.nidabutik.dto.PaymentResponse;
import org.example.nidabutik.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
public class PaymentController {
    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('USER', 'ADMIN')")
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponse pay(@Valid @RequestBody PaymentRequest request) {
        return paymentService.pay(request);
    }

    @PostMapping("/rollback-demo")
    @PreAuthorize("hasRole('ADMIN')")
    public PaymentResponse rollbackDemo(@Valid @RequestBody PaymentRequest request) {
        return paymentService.rollbackDemo(request);
    }
}
