package com.aisys.library.catalog;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/catalog")
public class CatalogController {
    private final CatalogService catalogService;

    public CatalogController(CatalogService catalogService) {
        this.catalogService = catalogService;
    }

    @GetMapping("/records")
    public List<BibliographicRecord> getAllRecords() {
        return catalogService.getAllRecords();
    }

    @PostMapping("/records")
    public BibliographicRecord createRecord(@RequestBody BibliographicRecord record, Principal principal) {
        String actor = (principal != null) ? principal.getName() : "SYSTEM";
        return catalogService.createRecord(record, actor);
    }
}