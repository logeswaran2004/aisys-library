package com.aisys.library.inventory;

import com.aisys.library.catalog.Item;
import com.aisys.library.catalog.ItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class InventoryService {
    private final ItemRepository itemRepository;

    public InventoryService(ItemRepository itemRepository) {
        this.itemRepository = itemRepository;
    }

    public List<Item> processRfidWandScan(List<String> scannedTagIds) {
        return itemRepository.findAll().stream()
                .filter(item -> scannedTagIds.contains(item.getRfidTagId()))
                .collect(Collectors.toList());
    }

    public Map<String, Object> reconcileShelf(List<String> expectedTagIds, List<String> scannedTagIds) {
        List<String> scanned = scannedTagIds == null ? List.of() : scannedTagIds;
        Set<String> scannedSet = new HashSet<>(scanned);

        List<String> expected = expectedTagIds == null || expectedTagIds.isEmpty()
                ? itemRepository.findAll().stream()
                    .map(Item::getRfidTagId)
                    .filter(tag -> tag != null && !tag.isBlank())
                    .toList()
                : expectedTagIds;

        List<String> missing = expected.stream().filter(tag -> !scannedSet.contains(tag)).toList();
        List<String> unexpected = scanned.stream().filter(tag -> !expected.contains(tag)).toList();
        List<String> matched = scanned.stream().filter(expected::contains).toList();

        List<Map<String, String>> foundItems = new ArrayList<>();
        for (String tag : matched) {
            itemRepository.findByRfidTagId(tag).ifPresent(item -> foundItems.add(Map.of(
                    "tagId", tag,
                    "accessionNumber", item.getAccessionNumber(),
                    "status", item.getStatus() == null ? "" : item.getStatus()
            )));
        }

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("expected", expected);
        result.put("found", matched);
        result.put("missing", missing);
        result.put("misplaced", unexpected);
        result.put("items", foundItems);
        result.put("audibleFeedback", missing.isEmpty() && unexpected.isEmpty() ? "OK" : "ALERT");
        return result;
    }
}
