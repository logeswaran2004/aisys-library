package com.aisys.library.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void logAction(String actor, String action, String resource, String result) {
        log.info("AUDIT | Actor: {} | Action: {} | Resource: {} | Result: {}", actor, action, resource, result);
        repository.save(new AuditLog(
                actor == null ? "SYSTEM" : actor,
                action == null ? "UNKNOWN" : action,
                resource == null ? "" : resource,
                result == null ? "" : result));
    }

    public List<AuditLog> search(String actor, String action, String resource) {
        return repository.search(
                blankToNull(actor),
                blankToNull(action),
                blankToNull(resource)
        );
    }

    public Optional<AuditLog> findById(Long id) {
        return repository.findById(id);
    }

    private static String blankToNull(String value) {
        return value == null || value.isBlank() ? null : value;
    }
}
