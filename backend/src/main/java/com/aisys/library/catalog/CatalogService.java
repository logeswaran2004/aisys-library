package com.aisys.library.catalog;

import com.aisys.library.audit.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

    public BibliographicRecord getRecord(Long id) {
        return biblioRepository.findById(id).orElseThrow(() -> new RuntimeException("Bibliographic record not found"));
    }

    public List<Item> getAllItems() {
        return itemRepository.findAll();
    }

    public Item getItem(Long id) {
        return itemRepository.findById(id).orElseThrow(() -> new RuntimeException("Item not found"));
    }

    public BibliographicRecord createRecord(BibliographicRecord record, String actor) {
        BibliographicRecord saved = biblioRepository.save(record);
        auditService.logAction(actor, "CREATE_BIBLIO_RECORD", record.getIsbn(), "SUCCESS");
        return saved;
    }

    @Transactional
    public BibliographicRecord updateRecord(Long id, BibliographicRecord incoming, String actor) {
        BibliographicRecord existing = getRecord(id);
        if (incoming.getIsbn() != null) existing.setIsbn(incoming.getIsbn());
        if (incoming.getTitle() != null) existing.setTitle(incoming.getTitle());
        if (incoming.getAuthor() != null) existing.setAuthor(incoming.getAuthor());
        if (incoming.getPublisher() != null) existing.setPublisher(incoming.getPublisher());
        if (incoming.getIsReference() != null) existing.setIsReference(incoming.getIsReference());
        BibliographicRecord saved = biblioRepository.save(existing);
        auditService.logAction(actor, "UPDATE_BIBLIO_RECORD", saved.getIsbn(), "SUCCESS");
        return saved;
    }

    @Transactional
    public void deleteRecord(Long id, String actor) {
        BibliographicRecord existing = getRecord(id);
        boolean inUse = itemRepository.findAll().stream()
                .anyMatch(item -> item.getBibliographicRecord() != null
                        && existing.getId().equals(item.getBibliographicRecord().getId()));
        if (inUse) {
            throw new RuntimeException("Cannot delete a bibliographic record that still has items");
        }
        biblioRepository.delete(existing);
        auditService.logAction(actor, "DELETE_BIBLIO_RECORD", existing.getIsbn(), "SUCCESS");
    }

    public Item createItem(Item item, String actor) {
        if (item.getBibliographicRecord() == null || item.getBibliographicRecord().getId() == null) {
            throw new RuntimeException("bibliographicRecord.id is required");
        }
        BibliographicRecord record = getRecord(item.getBibliographicRecord().getId());
        item.setBibliographicRecord(record);
        if (item.getStatus() == null || item.getStatus().isBlank()) {
            item.setStatus("AVAILABLE");
        }
        Item saved = itemRepository.save(item);
        auditService.logAction(actor, "CREATE_ITEM", item.getAccessionNumber(), "SUCCESS");
        return saved;
    }

    @Transactional
    public Item updateItem(Long id, Item incoming, String actor) {
        Item existing = getItem(id);
        if (incoming.getAccessionNumber() != null) existing.setAccessionNumber(incoming.getAccessionNumber());
        if (incoming.getBarcode() != null) existing.setBarcode(incoming.getBarcode());
        if (incoming.getRfidTagId() != null) existing.setRfidTagId(incoming.getRfidTagId());
        if (incoming.getStatus() != null) existing.setStatus(incoming.getStatus());
        if (incoming.getBibliographicRecord() != null && incoming.getBibliographicRecord().getId() != null) {
            existing.setBibliographicRecord(getRecord(incoming.getBibliographicRecord().getId()));
        }
        Item saved = itemRepository.save(existing);
        auditService.logAction(actor, "UPDATE_ITEM", saved.getAccessionNumber(), "SUCCESS");
        return saved;
    }

    @Transactional
    public void deleteItem(Long id, String actor) {
        Item existing = getItem(id);
        if ("ISSUED".equalsIgnoreCase(existing.getStatus())) {
            throw new RuntimeException("Cannot delete an item that is currently issued");
        }
        itemRepository.delete(existing);
        auditService.logAction(actor, "DELETE_ITEM", existing.getAccessionNumber(), "SUCCESS");
    }

    public Item saveItem(Item item, String actor, String action) {
        Item saved = itemRepository.save(item);
        auditService.logAction(actor, action, item.getAccessionNumber(), "SUCCESS");
        return saved;
    }

    public Optional<Item> getItemByBarcode(String barcode) {
        return itemRepository.findByBarcode(barcode);
    }
}
