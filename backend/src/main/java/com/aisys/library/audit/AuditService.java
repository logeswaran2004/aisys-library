package com.aisys.library.audit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class AuditService {
    private static final Logger log = LoggerFactory.getLogger(AuditService.class);
    private final AuditLogRepository repository;

    public AuditService(AuditLogRepository repository) {
        this.repository = repository;
    }

    public void logAction(String actor, String action, String resource, String result) {
        log.info("AUDIT | Actor: {} | Action: {} | Resource: {} | Result: {}", actor, action, resource, result);
        repository.save(new AuditLog(actor, action, resource, result));
    }
}