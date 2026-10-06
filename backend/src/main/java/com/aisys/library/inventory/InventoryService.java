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

    public List<Item> processRfidWandScan(List<String> scannedTagIds) {
        // Find all items matching the scanned RFID tags
        return itemRepository.findAll().stream()
                .filter(item -> scannedTagIds.contains(item.getRfidTagId()))
                .collect(Collectors.toList());
    }
}