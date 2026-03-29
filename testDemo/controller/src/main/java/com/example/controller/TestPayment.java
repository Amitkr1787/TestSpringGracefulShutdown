package com.example.controller;

import com.example.service.MerchantPaymentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/test-payment")
public class TestPayment {

    private final MerchantPaymentService merchantPaymentService;

    public TestPayment(MerchantPaymentService merchantPaymentService) {
        this.merchantPaymentService = merchantPaymentService;
    }

    @GetMapping("/process-payment")
    public String processPayment() {
        merchantPaymentService.processPayment("Merchant-A", 100.0);
        return "Payment processing started";
    }
}
