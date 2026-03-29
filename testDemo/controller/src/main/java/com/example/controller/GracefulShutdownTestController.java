package com.example.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/shutdown-test")
public class GracefulShutdownTestController {

    private static final Logger log = LoggerFactory.getLogger(GracefulShutdownTestController.class);

    @GetMapping("/slow")
    public String slow(@RequestParam(defaultValue = "15") int delaySeconds) throws InterruptedException {
        int seconds = Math.min(Math.max(delaySeconds, 1), 120);
        log.info("slow request started ({}s); trigger shutdown while this runs to verify graceful shutdown", seconds);
        Thread.sleep(seconds * 1000L);
        log.info("slow request finished after {}s", seconds);
        return "ok after " + seconds + "s";
    }
}
