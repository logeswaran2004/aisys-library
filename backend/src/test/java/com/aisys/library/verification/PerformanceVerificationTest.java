package com.aisys.library.verification;

import com.aisys.library.inventory.InventoryService;
import com.aisys.library.migration.DataMigrationService;
import com.aisys.library.migration.MigrationResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTimeout;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class PerformanceVerificationTest {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private DataMigrationService dataMigrationService;

    @Test
    public void testBulkInventoryProcessingPerformance() {
        List<String> tags = new ArrayList<>();
        for (int i = 0; i < 5000; i++) {
            tags.add("RFID-TAG-" + i);
        }
        assertTimeout(Duration.ofSeconds(5), () -> {
            Map<String, Object> result = inventoryService.reconcileShelf(tags, tags);
            assertEquals(5000, ((List<?>) result.get("found")).size());
            assertEquals("OK", result.get("audibleFeedback"));
        }, "Performance failure: InventoryService.reconcileShelf exceeded latency threshold");
    }

    @Test
    public void testTwentyThousandRecordMigrationChunking() {
        StringBuilder csv = new StringBuilder("Title,Author,Barcode,ISBN\n");
        for (int i = 0; i < 20_000; i++) {
            csv.append("Title ").append(i).append(",Author ").append(i)
                    .append(",PERF-BC-").append(i)
                    .append(",978-PERF-").append(i).append('\n');
        }
        byte[] bytes = csv.toString().getBytes(StandardCharsets.UTF_8);
        assertTimeout(Duration.ofSeconds(90), () -> {
            MigrationResult result = dataMigrationService.processMigration(
                    new ByteArrayInputStream(bytes), true, "perf-20k.csv", "PERF_TEST");
            assertEquals(20_000, result.totalProcessed);
            assertTrue(result.validRecords >= 20_000);
            assertEquals(0, result.invalidRecords);
        }, "20,000-row dry-run migration exceeded the performance SLA");
    }
}
