package com.example.service;

import com.example.common.dto.DemoDto;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

@Service
public class DemoService {

    private final List<DemoDto> demos = new ArrayList<>();
    private final AtomicLong nextId = new AtomicLong(1);

    public List<DemoDto> findAll() {
        synchronized (demos) {
            return List.copyOf(demos);
        }
    }

    public DemoDto create(String name) {
        DemoDto dto = new DemoDto(nextId.getAndIncrement(), name);
        synchronized (demos) {
            demos.add(dto);
        }
        return dto;
    }
}
