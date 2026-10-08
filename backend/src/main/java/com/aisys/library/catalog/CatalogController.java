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

    @GetMapping("/records/{id}")
    public BibliographicRecord getRecord(@PathVariable Long id) {
        return catalogService.getRecord(id);
    }

    @PostMapping("/records")
    public BibliographicRecord createRecord(@RequestBody BibliographicRecord record, Principal principal) {
        return catalogService.createRecord(record, actor(principal));
    }

    @PutMapping("/records/{id}")
    public BibliographicRecord updateRecord(@PathVariable Long id, @RequestBody BibliographicRecord record, Principal principal) {
        return catalogService.updateRecord(id, record, actor(principal));
    }

    @DeleteMapping("/records/{id}")
    public ResponseEntity<Void> deleteRecord(@PathVariable Long id, Principal principal) {
        catalogService.deleteRecord(id, actor(principal));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/items")
    public List<Item> getAllItems() {
        return catalogService.getAllItems();
    }

    @GetMapping("/items/{id}")
    public Item getItem(@PathVariable Long id) {
        return catalogService.getItem(id);
    }

    @PostMapping("/items")
    public Item createItem(@RequestBody Item item, Principal principal) {
        return catalogService.createItem(item, actor(principal));
    }

    @PutMapping("/items/{id}")
    public Item updateItem(@PathVariable Long id, @RequestBody Item item, Principal principal) {
        return catalogService.updateItem(id, item, actor(principal));
    }

    @DeleteMapping("/items/{id}")
    public ResponseEntity<Void> deleteItem(@PathVariable Long id, Principal principal) {
        catalogService.deleteItem(id, actor(principal));
        return ResponseEntity.noContent().build();
    }

    private static String actor(Principal principal) {
        return principal != null ? principal.getName() : "SYSTEM";
    }
}
