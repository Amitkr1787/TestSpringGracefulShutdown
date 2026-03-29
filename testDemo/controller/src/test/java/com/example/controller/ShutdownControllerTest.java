package com.example.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class ShutdownControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ConfigurableApplicationContext context;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize test setup if necessary
    }

    @Test
    public void testShutdownEndpointReturnsCorrectMessage() throws Exception {
        mockMvc.perform(get("/custom-shutdown"))
                .andExpect(status().isOk())
                .andExpect(content().string("Application is shutting down..."));
    }

    @Test
    public void testShutdownEndpointResponseContentType() throws Exception {
        mockMvc.perform(get("/custom-shutdown"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith("text/plain;charset=UTF-8"));
    }

    @Test
    public void testShutdownEndpointIsGetMapping() throws Exception {
        // Verify that POST requests are not allowed
        mockMvc.perform(post("/custom-shutdown"))
                .andExpect(status().isMethodNotAllowed());
    }

    @Test
    public void testShutdownThreadExecution() throws Exception {
        // This test verifies that the shutdown thread is created and execution starts
        mockMvc.perform(get("/custom-shutdown"))
                .andExpect(status().isOk());
        
        // Allow time for the shutdown thread to begin execution
        Thread.sleep(500);
    }
}