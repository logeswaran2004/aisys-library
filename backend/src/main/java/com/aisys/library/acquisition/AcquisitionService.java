package com.aisys.library.acquisition;

import com.aisys.library.audit.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class AcquisitionService {
    private final AcquisitionRepository repository;
    private final AuditService auditService;

    public AcquisitionService(AcquisitionRepository repository, AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    public List<Acquisition> list() {
        return repository.findAll();
    }

    public Acquisition get(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Acquisition not found"));
    }

    @Transactional
    public Acquisition create(Acquisition acquisition, String actor) {
        validate(acquisition);
        if (acquisition.getStatus() == null || acquisition.getStatus().isBlank()) {
            acquisition.setStatus("ORDERED");
        }
        Acquisition saved = repository.save(acquisition);
        auditService.logAction(actor, "CREATE_ACQUISITION", saved.getPoNumber(), "SUCCESS");
        return saved;
    }

    @Transactional
    public Acquisition update(Long id, Acquisition incoming, String actor) {
        Acquisition existing = get(id);
        if (incoming.getTitle() != null) existing.setTitle(incoming.getTitle());
        if (incoming.getVendor() != null) existing.setVendor(incoming.getVendor());
        if (incoming.getPoNumber() != null) existing.setPoNumber(incoming.getPoNumber());
        if (incoming.getCost() != null) existing.setCost(incoming.getCost());
        if (incoming.getStatus() != null) existing.setStatus(incoming.getStatus());
        validate(existing);
        Acquisition saved = repository.save(existing);
        auditService.logAction(actor, "UPDATE_ACQUISITION", saved.getPoNumber(), "SUCCESS");
        return saved;
    }

    @Transactional
    public void delete(Long id, String actor) {
        Acquisition existing = get(id);
        repository.delete(existing);
        auditService.logAction(actor, "DELETE_ACQUISITION", existing.getPoNumber(), "SUCCESS");
    }

    private void validate(Acquisition acquisition) {
        if (acquisition.getTitle() == null || acquisition.getTitle().isBlank()) {
            throw new RuntimeException("Acquisition title is required");
        }
        if (acquisition.getVendor() == null || acquisition.getVendor().isBlank()) {
            throw new RuntimeException("Acquisition vendor is required");
        }
    }
}
