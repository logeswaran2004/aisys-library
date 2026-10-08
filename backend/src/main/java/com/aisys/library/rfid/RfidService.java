package com.aisys.library.rfid;

import com.aisys.library.audit.AuditService;
import com.aisys.library.catalog.Item;
import com.aisys.library.catalog.ItemRepository;
import com.aisys.library.integration.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class RfidService {
    private final RfidReader rfidReader;
    private final ItemRepository itemRepository;
    private final SecurityGateEventRepository gateRepository;
    private final AuditService auditService;
    private final CameraSubsystem camera;
    private final NotificationService notifier;

    public RfidService(RfidReader rfidReader, ItemRepository itemRepository, 
                       SecurityGateEventRepository gateRepository, AuditService auditService,
                       CameraSubsystem camera, NotificationService notifier) {
        this.rfidReader = rfidReader;
        this.itemRepository = itemRepository;
        this.gateRepository = gateRepository;
        this.auditService = auditService;
        this.camera = camera;
        this.notifier = notifier;
    }

    @Transactional
    public void handleGateAlarm(String tagId, String gateId) {
        // D6: Duplicate Event Suppression (Prevent alarm floods within 10 seconds)
        Instant tenSecondsAgo = Instant.now().minus(10, ChronoUnit.SECONDS);
        long recentAlarms = gateRepository.findAll().stream()
            .filter(e -> e.getRfidTagId() != null && e.getRfidTagId().equals(tagId))
            .filter(e -> e.getTimestamp().isAfter(tenSecondsAgo))
            .count();

        if (recentAlarms > 0) {
            return; // Suppress duplicate
        }

        // D6: Retry Logic & Offline Behavior Simulation
        String imageUrl = null;
        int retries = 3;
        while (retries > 0) {
            try {
                imageUrl = camera.captureGateImage(gateId); // Simulating network call
                break;
            } catch (Exception e) {
                retries--;
                if (retries == 0) imageUrl = "OFFLINE_NO_IMAGE";
            }
        }

        Optional<Item> itemOpt = itemRepository.findByRfidTagId(tagId);
        
        String accession = itemOpt.map(Item::getAccessionNumber).orElse("UNKNOWN_TAG");
        
        SecurityGateEvent event = new SecurityGateEvent(gateId, tagId, accession, "UNAUTHORIZED_REMOVAL", imageUrl);
        gateRepository.save(event);
        
        notifier.sendAlert("Security Gate " + gateId + " breached by tag " + tagId);
        auditService.logAction("SYSTEM", "GATE_ALARM", tagId, "TRIGGERED");
    }
}
