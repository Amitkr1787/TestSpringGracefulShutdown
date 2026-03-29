package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ShutdownController {

    @Autowired
    private ConfigurableApplicationContext context;

    @GetMapping("/custom-shutdown")
    public String shutdownApp() {
        // Start a new thread to avoid closing the context before the response is sent
        Thread shutdownThread = new Thread(() -> {
            try {
                Thread.sleep(1000); // Give the server time to send the response
            } catch (InterruptedException ignored) {
            }
            context.close();
        });
        shutdownThread.setDaemon(false);
        shutdownThread.start();

        return "Application is shutting down...";
    }
}
