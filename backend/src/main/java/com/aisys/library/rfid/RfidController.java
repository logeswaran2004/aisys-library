package com.aisys.library.rfid;

import com.aisys.library.catalog.CatalogService;
import com.aisys.library.catalog.Item;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/rfid", "/api/rfid"})
public class RfidController {
    private final RfidService rfidService;
    private final TagCommissioningService tagCommissioningService;
    private final CatalogService catalogService;
    private final RfidReader rfidReader;
    private final SecurityGateEventRepository gateEventRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final OfflineEventService offlineEventService;

    public RfidController(RfidService rfidService, TagCommissioningService tagCommissioningService,
                          CatalogService catalogService, RfidReader rfidReader,
                          SecurityGateEventRepository gateEventRepository,
                          ApplicationEventPublisher eventPublisher,
                          OfflineEventService offlineEventService) {
        this.rfidService = rfidService;
        this.tagCommissioningService = tagCommissioningService;
        this.catalogService = catalogService;
        this.rfidReader = rfidReader;
        this.gateEventRepository = gateEventRepository;
        this.eventPublisher = eventPublisher;
        this.offlineEventService = offlineEventService;
    }

    @PostMapping("/event")
    public ResponseEntity<String> handleGateEvent(@RequestBody Map<String, String> payload) {
        return handleGateAlarm(payload);
    }

    @PostMapping("/gate-event")
    public ResponseEntity<String> handleGateAlarm(@RequestBody Map<String, String> payload) {
        String tagId = firstNonBlank(payload.get("tagId"), payload.get("rfidTagId"), payload.get("accessionNumber"));
        String gateId = firstNonBlank(payload.get("gateId"), payload.get("deviceId"), "GATE-MAIN");
        rfidService.handleGateAlarm(tagId, gateId);
        eventPublisher.publishEvent(new GateAlarmEvent(this, tagId));
        return ResponseEntity.ok("RFID Event Processed Successfully");
    }

    @PostMapping("/tag")
    public ResponseEntity<?> commissionTag(@RequestBody Map<String, String> payload) {
        String barcode = payload.get("barcode");
        String tagId = firstNonBlank(payload.get("tagId"), payload.get("rfidTagId"));
        Item item = catalogService.getItemByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Item not found for barcode " + barcode));
        boolean written = rfidReader.writeTag(tagId, item.getAccessionNumber());
        boolean encoded = tagCommissioningService.encodeNewTag(item.getAccessionNumber(), tagId);
        if (written && encoded) {
            item.setRfidTagId(tagId);
            catalogService.saveItem(item, "RFID_STAFF", "RFID_TAG_ASSOCIATE");
        }
        return ResponseEntity.ok(Map.of(
                "accessionNumber", item.getAccessionNumber(),
                "rfidTagId", tagId,
                "written", written
        ));
    }

    @GetMapping("/scan")
    public List<String> scanReader() {
        rfidReader.connect();
        return rfidReader.readTags();
    }

    @GetMapping("/gate-events")
    public List<SecurityGateEvent> gateEvents() {
        return gateEventRepository.findAll();
    }

    @PostMapping("/offline/retry")
    public Map<String, Object> retryOffline() {
        boolean processed = offlineEventService.processRetry();
        return Map.of("processed", processed, "pending", offlineEventService.getPendingEventCount());
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }
        return null;
    }
}
