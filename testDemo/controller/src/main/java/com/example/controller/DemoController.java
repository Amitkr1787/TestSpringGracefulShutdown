package com.example.controller;

import com.example.common.dto.DemoDto;
import com.example.service.DemoService;
import java.util.List;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/demos")
public class DemoController {

    private final DemoService demoService;

    public DemoController(DemoService demoService) {
        this.demoService = demoService;
    }

    @GetMapping
    public List<DemoDto> list() {
        return demoService.findAll();
    }

    public record CreateDemoRequest(String name) {
    }

    @PostMapping
    public DemoDto create(@RequestBody CreateDemoRequest body) {
        return demoService.create(body.name());
    }
}
