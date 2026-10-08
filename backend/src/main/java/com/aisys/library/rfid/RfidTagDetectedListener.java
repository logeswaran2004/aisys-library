package com.aisys.library.rfid;

import com.aisys.library.audit.AuditService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class RfidTagDetectedListener {
    private static final Logger log = LoggerFactory.getLogger(RfidTagDetectedListener.class);
    private final AuditService auditService;

    public RfidTagDetectedListener(AuditService auditService) {
        this.auditService = auditService;
    }

    @EventListener
    public void onTagDetected(RfidTagDetectedEvent event) {
        log.info("RFID tag detected: {} via {}", event.getTagId(), event.getOrigin());
        auditService.logAction("RFID", "TAG_DETECTED", event.getTagId(), event.getOrigin());
    }
}
