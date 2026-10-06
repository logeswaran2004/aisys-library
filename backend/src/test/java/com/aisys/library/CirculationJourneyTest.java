package com.aisys.library;

import com.aisys.library.circulation.CirculationService;
import com.aisys.library.circulation.CirculationTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driverClassName=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password=",
    "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
    "spring.flyway.enabled=true"
})
class CirculationJourneyTest {

    @Autowired
    private CirculationService circulationService;

    @Test
    void demonstrateCheckoutJourney() {
        // Step 1: Attempt to checkout an item to a BLOCKED member (M1002 from our V2 seed data)
        Exception exception = assertThrows(RuntimeException.class, () -> {
            circulationService.checkout("BC-1001", "M1002", "TEST_STAFF");
        });
        assertTrue(exception.getMessage().contains("AC04: Member account is blocked."));

        // Step 2: Successfully checkout an item to an ACTIVE member (M1001)
        CirculationTransaction tx = circulationService.checkout("BC-1001", "M1001", "TEST_STAFF");
        assertNotNull(tx.getId());
        assertEquals("ISSUED", tx.getItem().getStatus());
        assertEquals("ACTIVE", tx.getStatus());

        // Step 3: Attempt to checkout the same item again (Should fail since it's not AVAILABLE)
        Exception notAvailableEx = assertThrows(RuntimeException.class, () -> {
            circulationService.checkout("BC-1001", "M1001", "TEST_STAFF");
        });
        assertTrue(notAvailableEx.getMessage().contains("Item is not available for checkout."));
    }
}