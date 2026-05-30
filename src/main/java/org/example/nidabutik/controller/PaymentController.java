package org.example.nidabutik.controller;

import jakarta.validation.Valid;
import org.example.nidabutik.dto.PaymentRequest;
import org.example.nidabutik.dto.PaymentResponse;
import org.example.nidabutik.service.PaymentService;
import org.springframework.http.HttpStatus;
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
    public List<PaymentResponse> getAllPayments() {
        return paymentService.getAllPayments();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public PaymentResponse pay(@Valid @RequestBody PaymentRequest request) {
        return paymentService.pay(request);
    }

    @PostMapping("/rollback-demo")
    public PaymentResponse rollbackDemo(@Valid @RequestBody PaymentRequest request) {
        return paymentService.rollbackDemo(request);
    }
}
