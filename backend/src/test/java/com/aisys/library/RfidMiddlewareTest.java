package com.aisys.library;

import com.aisys.library.rfid.RfidService;
import com.aisys.library.rfid.SecurityGateEventRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb2;DB_CLOSE_DELAY=-1",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.flyway.enabled=true"
})
class RfidMiddlewareTest {

    @Autowired
    private RfidService rfidService;

    @Autowired
    private SecurityGateEventRepository gateRepository;

    @Test
    void testDuplicateEventSuppression() {
        long initialCount = gateRepository.count();
        
        // Trigger alarm
        rfidService.handleGateAlarm("TAG-999", "GATE-NORTH");
        assertEquals(initialCount + 1, gateRepository.count(), "First alarm should be saved");

        // Trigger identical alarm immediately (should be suppressed)
        rfidService.handleGateAlarm("TAG-999", "GATE-NORTH");
        assertEquals(initialCount + 1, gateRepository.count(), "Duplicate alarm within 10s should be suppressed");
    }
}