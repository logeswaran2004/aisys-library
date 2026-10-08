package com.aisys.library.rfid;

import com.aisys.library.audit.AuditService;
import com.aisys.library.catalog.Item;
import com.aisys.library.catalog.ItemRepository;
import com.aisys.library.notification.NotificationProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class SecurityGateListener {
    private static final Logger log = LoggerFactory.getLogger(SecurityGateListener.class);
    private final ItemRepository itemRepository;
    private final AuditService auditService;
    private final NotificationProvider notificationProvider;

    public SecurityGateListener(ItemRepository itemRepository, AuditService auditService, NotificationProvider notificationProvider) {
        this.itemRepository = itemRepository;
        this.auditService = auditService;
        this.notificationProvider = notificationProvider;
    }

    @Async
    @EventListener
    public void handleGateAlarm(GateAlarmEvent event) {
        log.warn("GATE ALARM TRIGGERED for Tag: {}", event.getTagId());
        
        // AC06: Identify Item & Check Circulation Status
        Optional<Item> itemOpt = itemRepository.findByRfidTagId(event.getTagId())
                .or(() -> itemRepository.findByBarcode(event.getTagId()));
        if (itemOpt.isPresent()) {
            Item item = itemOpt.get();
            if (!"ISSUED".equals(item.getStatus())) {
                auditService.logAction("SECURITY_GATE", "UNAUTHORIZED_REMOVAL", item.getAccessionNumber(), "ALARM");
                notificationProvider.sendEmail("admin@library.local", "Security Alert", "Unauthorized item removal: " + item.getAccessionNumber());
            }
        } else {
            auditService.logAction("SECURITY_GATE", "UNKNOWN_TAG_ALARM", event.getTagId(), "ALARM");
        }
    }
}