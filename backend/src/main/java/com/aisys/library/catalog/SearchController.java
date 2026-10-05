package com.aisys.library.catalog;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
public class SearchController {
    private final BibliographicRecordRepository repository;

    public SearchController(BibliographicRecordRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<BibliographicRecord> searchCatalog(@RequestParam String query) {
        return repository.findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(query, query);
    }
}