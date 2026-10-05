package com.aisys.library.catalog;

import com.aisys.library.audit.AuditService;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class CatalogService {
    private final BibliographicRecordRepository biblioRepository;
    private final ItemRepository itemRepository;
    private final AuditService auditService;

    public CatalogService(BibliographicRecordRepository biblioRepository, ItemRepository itemRepository, AuditService auditService) {
        this.biblioRepository = biblioRepository;
        this.itemRepository = itemRepository;
        this.auditService = auditService;
    }

    public List<BibliographicRecord> getAllRecords() {
        return biblioRepository.findAll();
    }

    public BibliographicRecord createRecord(BibliographicRecord record, String actor) {
        BibliographicRecord saved = biblioRepository.save(record);
        auditService.logAction(actor, "CREATE_BIBLIO_RECORD", record.getIsbn(), "SUCCESS");
        return saved;
    }

    public Item createItem(Item item, String actor) {
        Item saved = itemRepository.save(item);
        auditService.logAction(actor, "CREATE_ITEM", item.getAccessionNumber(), "SUCCESS");
        return saved;
    }
    
    public Optional<Item> getItemByBarcode(String barcode) {
        return itemRepository.findByBarcode(barcode);
    }
}