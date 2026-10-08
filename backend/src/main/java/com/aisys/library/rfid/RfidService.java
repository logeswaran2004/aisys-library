package com.aisys.library.rfid;

import com.aisys.library.audit.AuditService;
import com.aisys.library.catalog.Item;
import com.aisys.library.catalog.ItemRepository;
import com.aisys.library.integration.CameraSubsystem;
import com.aisys.library.integration.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@Service
public class RfidService {
    private static final Logger log = LoggerFactory.getLogger(RfidService.class);
    private final RfidReader rfidReader;
    private final ItemRepository itemRepository;
    private final SecurityGateEventRepository gateRepository;
    private final AuditService auditService;
    private final CameraSubsystem camera;
    private final NotificationService notifier;
    private final OfflineEventService offlineEventService;
    private final ApplicationEventPublisher eventPublisher;

    public RfidService(RfidReader rfidReader, ItemRepository itemRepository,
                       SecurityGateEventRepository gateRepository, AuditService auditService,
                       CameraSubsystem camera, NotificationService notifier,
                       OfflineEventService offlineEventService, ApplicationEventPublisher eventPublisher) {
        this.rfidReader = rfidReader;
        this.itemRepository = itemRepository;
        this.gateRepository = gateRepository;
        this.auditService = auditService;
        this.camera = camera;
        this.notifier = notifier;
        this.offlineEventService = offlineEventService;
        this.eventPublisher = eventPublisher;
    }

    public void handleGateAlarm(String tagId, String gateId) {
        eventPublisher.publishEvent(new RfidTagDetectedEvent(this, tagId, "SECURITY_GATE"));

        String imageUrl = captureWithRetry(gateId);
        String accession = "UNKNOWN_TAG";
        try {
            Instant tenSecondsAgo = Instant.now().minus(10, ChronoUnit.SECONDS);
            long recentAlarms = gateRepository.findAll().stream()
                .filter(e -> e.getRfidTagId() != null && e.getRfidTagId().equals(tagId))
                .filter(e -> e.getTimestamp() != null && e.getTimestamp().isAfter(tenSecondsAgo))
                .count();
            if (recentAlarms > 0) {
                return;
            }

            Optional<Item> itemOpt = itemRepository.findByRfidTagId(tagId);
            accession = itemOpt.map(Item::getAccessionNumber).orElse("UNKNOWN_TAG");

            SecurityGateEvent event = new SecurityGateEvent(gateId, tagId, accession, "UNAUTHORIZED_REMOVAL", imageUrl);
            gateRepository.save(event);

            notifier.sendAlert("Security Gate " + gateId + " breached by tag " + tagId);
            auditService.logAction("SYSTEM", "GATE_ALARM", tagId, "TRIGGERED");
        } catch (RuntimeException ex) {
            if (!isPersistenceFailure(ex)) {
                throw ex;
            }
            log.warn("Persisting gate alarm failed; queueing for retry: {}", ex.getMessage());
            offlineEventService.queueEventForRetry(
                    new SecurityGateEvent(gateId, tagId, accession, "OFFLINE_QUEUED", imageUrl));
        }
    }

    public void publishDetectedTags(Iterable<String> tagIds, String origin) {
        if (tagIds == null) {
            return;
        }
        for (String tagId : tagIds) {
            if (tagId != null && !tagId.isBlank()) {
                eventPublisher.publishEvent(new RfidTagDetectedEvent(this, tagId, origin));
            }
        }
    }

    static boolean isPersistenceFailure(Throwable ex) {
        Throwable cursor = ex;
        while (cursor != null) {
            if (cursor instanceof DataAccessException
                    || cursor instanceof jakarta.persistence.PersistenceException) {
                return true;
            }
            cursor = cursor.getCause();
        }
        return false;
    }

    private String captureWithRetry(String gateId) {
        String imageUrl = null;
        int retries = 3;
        while (retries > 0) {
            try {
                imageUrl = camera.captureGateImage(gateId);
                break;
            } catch (Exception e) {
                retries--;
                if (retries == 0) {
                    imageUrl = "OFFLINE_NO_IMAGE";
                }
            }
        }
        return imageUrl;
    }
}
