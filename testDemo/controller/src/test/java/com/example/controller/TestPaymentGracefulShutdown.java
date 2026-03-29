package com.example.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.web.servlet.MockMvc;

import com.example.service.MerchantPaymentService;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.mock;

@SpringBootTest
@AutoConfigureMockMvc
public class TestPaymentGracefulShutdown {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() {
        System.out.println("=== Test Setup Started ===");
    }

    @Test
    public void testPaymentAndGracefulShutdown() throws Exception {
        // Step 1: Call the processPayment method
        System.out.println("\n=== Starting Payment Processing ===");
        System.out.println("Time: " + System.currentTimeMillis());

        mockMvc.perform(get("/api/test-payment/process-payment"))
                .andExpect(status().isOk())
                .andExpect(content().string("Payment processing started"));

        System.out.println("✓ Payment Response: Payment processing started");

        // Step 2: Wait for 30 seconds to allow payment processing to complete
        System.out.println("\n=== Waiting 30 seconds for payment processing ===");
        System.out.println("Wait started at: " + System.currentTimeMillis());
        Thread.sleep(30000);

        System.out.println("✓ 30 seconds elapsed at: " + System.currentTimeMillis());
        System.out.println("✓ Payment processing should be complete.");

        // Step 3: Call the shutdownApp method after 30 seconds
        System.out.println("\n=== Initiating Graceful Shutdown ===");
        System.out.println("Shutdown initiated at: " + System.currentTimeMillis());

        mockMvc.perform(get("/custom-shutdown"))
                .andExpect(status().isOk())
                .andExpect(content().string("Application is shutting down..."));

        System.out.println("✓ Shutdown Response: Application is shutting down...");
        System.out.println("=== Graceful Shutdown Test Completed Successfully ===\n");
    }

    @Configuration
    static class TestConfig {
        @Bean
        public MerchantPaymentService merchantPaymentService() {
            return mock(MerchantPaymentService.class);
        }

        @Bean
        public TestPayment testPayment(MerchantPaymentService merchantPaymentService) {
            return new TestPayment(merchantPaymentService);
        }

        @Bean
        public ShutdownController shutdownController() {
            return new ShutdownController();
        }
    }
}
