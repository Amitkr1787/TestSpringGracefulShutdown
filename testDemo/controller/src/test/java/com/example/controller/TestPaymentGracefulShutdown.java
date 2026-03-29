package com.example.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestPaymentGracefulShutdown {

    @Autowired
    private TestRestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        // Initialize test setup if necessary
    }

    @Test
    public void testPaymentAndGracefulShutdown() throws InterruptedException {
        // Step 1: Call the processPayment method
        System.out.println("=== Starting Payment Processing ===");
        System.out.println("Time: " + System.currentTimeMillis());
        
        ResponseEntity<String> paymentResponse = restTemplate.getForEntity(
                "/api/test-payment/process-payment", 
                String.class
        );
        
        System.out.println("Payment Response Status: " + paymentResponse.getStatusCode());
        System.out.println("Payment Response Body: " + paymentResponse.getBody());

        // Step 2: Wait for 30 seconds to allow payment processing to complete
        System.out.println("\n=== Waiting 30 seconds for payment processing ===");
        Thread.sleep(30000);
        
        System.out.println("30 seconds elapsed. Payment processing should be complete.");

        // Step 3: Call the shutdownApp method after 30 seconds
        System.out.println("\n=== Initiating Graceful Shutdown ===");
        System.out.println("Time: " + System.currentTimeMillis());
        
        ResponseEntity<String> shutdownResponse = restTemplate.getForEntity(
                "/custom-shutdown", 
                String.class
        );
        
        System.out.println("Shutdown Response Status: " + shutdownResponse.getStatusCode());
        System.out.println("Shutdown Response Body: " + shutdownResponse.getBody());
        System.out.println("=== Graceful Shutdown Test Completed ===");
    }
}