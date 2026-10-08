package com.aisys.library.acquisition;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/acquisitions")
public class AcquisitionController {
    private final AcquisitionService acquisitionService;

    public AcquisitionController(AcquisitionService acquisitionService) {
        this.acquisitionService = acquisitionService;
    }

    @GetMapping
    public List<Acquisition> list() {
        return acquisitionService.list();
    }

    @GetMapping("/{id}")
    public Acquisition get(@PathVariable Long id) {
        return acquisitionService.get(id);
    }

    @PostMapping
    public Acquisition create(@RequestBody Acquisition acquisition, Principal principal) {
        return acquisitionService.create(acquisition, actor(principal));
    }

    @PutMapping("/{id}")
    public Acquisition update(@PathVariable Long id, @RequestBody Acquisition acquisition, Principal principal) {
        return acquisitionService.update(id, acquisition, actor(principal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id, Principal principal) {
        acquisitionService.delete(id, actor(principal));
        return ResponseEntity.noContent().build();
    }

    private static String actor(Principal principal) {
        return principal != null ? principal.getName() : "SYSTEM";
    }
}
