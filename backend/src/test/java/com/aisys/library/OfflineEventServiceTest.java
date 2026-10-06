package com.aisys.library;

import com.aisys.library.rfid.OfflineEventService;
import com.aisys.library.rfid.SecurityGateEvent;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class OfflineEventServiceTest {

    @Test
    public void testOfflineQueuingAndRetry() {
        OfflineEventService service = new OfflineEventService();
        SecurityGateEvent mockEvent = new SecurityGateEvent("GATE-FRONT", "RFID-999", "ACC-12345", "OFFLINE_QUEUED", "CCTV-001");
        
        service.queueEventForRetry(mockEvent);
        assertEquals(1, service.getPendingEventCount(), "Event should be safely queued offline");
        
        boolean processed = service.processRetry();
        assertTrue(processed, "Event should be successfully retried and sent to main system");
        assertEquals(0, service.getPendingEventCount(), "Queue should be empty after successful retry");
    }
}