package com.aisys.library.inventory;

import com.aisys.library.catalog.Item;
import com.aisys.library.catalog.ItemRepository;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final ItemRepository itemRepository;

    public InventoryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public InventoryReport processShelfScan(List<String> scannedTags, List<String> expectedBarcodes) {
        List<Item> expectedItems = itemRepository.findAll().stream()
                .filter(i -> expectedBarcodes.contains(i.getBarcode()))
                .collect(Collectors.toList());

        long missingCount = expectedItems.stream()
                .filter(i -> !scannedTags.contains(i.getBarcode()))
                .count();

        long misplacedCount = scannedTags.stream()
                .filter(tag -> !expectedBarcodes.contains(tag))
                .count();

        return new InventoryReport(expectedItems.size(), scannedTags.size(), missingCount, misplacedCount);
    }
    
    public record InventoryReport(int expected, int scanned, long missing, long misplaced) {}
}