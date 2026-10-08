package com.aisys.library.serial;

import com.aisys.library.audit.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class SerialService {
    private final SerialRepository repository;
    private final AuditService auditService;

    public SerialService(SerialRepository repository, AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    public List<Serial> list() {
        return repository.findAll();
    }

    public Serial get(Long id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("Serial not found"));
    }

    @Transactional
    public Serial create(Serial serial, String actor) {
        validate(serial);
        Serial saved = repository.save(serial);
        auditService.logAction(actor, "CREATE_SERIAL", saved.getIssn(), "SUCCESS");
        return saved;
    }

    @Transactional
    public Serial update(Long id, Serial incoming, String actor) {
        Serial existing = get(id);
        if (incoming.getTitle() != null) existing.setTitle(incoming.getTitle());
        if (incoming.getIssn() != null) existing.setIssn(incoming.getIssn());
        if (incoming.getFrequency() != null) existing.setFrequency(incoming.getFrequency());
        validate(existing);
        Serial saved = repository.save(existing);
        auditService.logAction(actor, "UPDATE_SERIAL", saved.getIssn(), "SUCCESS");
        return saved;
    }

    @Transactional
    public void delete(Long id, String actor) {
        Serial existing = get(id);
        repository.delete(existing);
        auditService.logAction(actor, "DELETE_SERIAL", existing.getIssn(), "SUCCESS");
    }

    private void validate(Serial serial) {
        if (serial.getTitle() == null || serial.getTitle().isBlank()) {
            throw new RuntimeException("Serial title is required");
        }
        if (serial.getIssn() == null || serial.getIssn().isBlank()) {
            throw new RuntimeException("Serial ISSN is required");
        }
    }
}
