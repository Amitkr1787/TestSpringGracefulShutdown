package com.example.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class TestPaymentTest {

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize test data or mock behavior if necessary
    }

    @Test
    public void testCreatePayment() throws Exception {
        mockMvc.perform(post("/payment")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 1000, \"currency\": \"USD\"}")).
                andExpect(status().isCreated());
    }

    @Test
    public void testGetPayment() throws Exception {
        mockMvc.perform(get("/payment/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amount").value(1000))
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    public void testUpdatePayment() throws Exception {
        mockMvc.perform(put("/payment/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"amount\": 2000, \"currency\": \"USD\"}"))
                .andExpect(status().isOk());
    }

    @Test
    public void testDeletePayment() throws Exception {
        mockMvc.perform(delete("/payment/1"))
                .andExpect(status().isNoContent());
    }
}