package com.aisys.library.migration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.core.io.ClassPathResource;
import java.io.InputStream;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = {
    "spring.datasource.url=jdbc:h2:mem:migrationtestdb;DB_CLOSE_DELAY=-1",
    "spring.datasource.driver-class-name=org.h2.Driver",
    "spring.datasource.username=sa",
    "spring.datasource.password="
})
public class MigrationDatabaseTest {

    @Autowired
    private DataMigrationService migrationService;

    @Autowired
    private LegacyItemRepository repository;

    @Test
    @Transactional 
    public void testExecuteMigrationAndDemonstrateRollback() throws Exception {
        long initialCount = repository.count();
        assertEquals(0, initialCount, "Database should be empty before migration");

        // Safely load the CSV using Spring's resource loader
        InputStream csvStream = new ClassPathResource("legacy_records.csv").getInputStream();
        MigrationResult result = migrationService.processMigration(csvStream, false);

        // Verify counts
        assertEquals(2, result.validRecords, "Should process 2 valid records");
        assertEquals(2, repository.count(), "Should have successfully saved 2 records to the test database");
    }
}
