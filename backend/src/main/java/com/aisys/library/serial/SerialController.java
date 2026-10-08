package com.aisys.library.serial;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/serials")
public class SerialController {
    private final SerialRepository repository;

    public SerialController(SerialRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Serial> list() {
        return repository.findAll();
    }

    @PostMapping
    public Serial create(@RequestBody Serial serial) {
        return repository.save(serial);
    }
}
