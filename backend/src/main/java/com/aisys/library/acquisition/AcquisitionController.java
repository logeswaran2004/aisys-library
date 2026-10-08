package com.aisys.library.acquisition;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/acquisitions")
public class AcquisitionController {
    private final AcquisitionRepository repository;

    public AcquisitionController(AcquisitionRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Acquisition> list() {
        return repository.findAll();
    }

    @PostMapping
    public Acquisition create(@RequestBody Acquisition acquisition) {
        return repository.save(acquisition);
    }
}
