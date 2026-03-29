package com.example.service;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class MerchantPaymentService {

    // Inject merchant client (e.g., Stripe, PayPal)
    // private final StripeClient stripeClient;

    @Async("paymentExecutor")
    public void processPayment(String merchantId, double amount) {
        System.out.println(
                "Processing payment: " + amount + " for " + merchantId + " on " + Thread.currentThread().getName());
        try {
            // Simulate API call to merchant processor
            Thread.sleep(20000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.println("Finished payment for " + merchantId);
    }
}
