package com.aisys.library.inventory;

import com.aisys.library.rfid.InventoryScannerService;
import com.aisys.library.rfid.RfidReader;
import com.aisys.library.rfid.RfidService;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    private final InventoryScannerService scannerService;
    private final RfidReader rfidReader;
    private final RfidService rfidService;

    public InventoryController(InventoryService inventoryService, InventoryScannerService scannerService,
                               RfidReader rfidReader, RfidService rfidService) {
        this.inventoryService = inventoryService;
        this.scannerService = scannerService;
        this.rfidReader = rfidReader;
        this.rfidService = rfidService;
    }

    @GetMapping("/scan")
    public Map<String, Object> scan(
            @RequestParam(name = "tags", required = false) String tags,
            @RequestParam(name = "expected", required = false) String expected) {
        List<String> scanned = split(tags);
        if (scanned.isEmpty()) {
            scanned = rfidReader.readTags();
        }
        scannerService.processBatchTagging(scanned);
        rfidService.publishDetectedTags(scanned, "INVENTORY_SCAN");
        return inventoryService.reconcileShelf(split(expected), scanned);
    }

    @PostMapping("/scan")
    public Map<String, Object> scanBody(@RequestBody ScanRequest request) {
        List<String> scanned = request.tags() == null ? rfidReader.readTags() : request.tags();
        scannerService.processBatchTagging(scanned);
        rfidService.publishDetectedTags(scanned, "INVENTORY_SCAN");
        return inventoryService.reconcileShelf(request.expected(), scanned);
    }

    public record ScanRequest(List<String> tags, List<String> expected) {}

    private static List<String> split(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }
}
