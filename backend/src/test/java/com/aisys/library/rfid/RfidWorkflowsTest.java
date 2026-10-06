package com.aisys.library.rfid;

import org.junit.jupiter.api.Test;
import java.util.Arrays;
import static org.junit.jupiter.api.Assertions.*;

public class RfidWorkflowsTest {

    @Test
    public void testSmartCardAuthentication() {
        SmartCardAuthService authService = new SmartCardAuthService();
        assertTrue(authService.authenticatePatron("SC-987654321"), "Valid smart card should authenticate");
        assertFalse(authService.authenticatePatron("INVALID-CARD"), "Invalid smart card should fail");
    }

    @Test
    public void testInventoryBatchTagging() {
        InventoryScannerService scannerService = new InventoryScannerService();
        int processed = scannerService.processBatchTagging(Arrays.asList("TAG-1", "TAG-2", "TAG-3"));
        assertEquals(3, processed, "Should process all tags in the inventory batch");
    }
}