package com.aisys.library.migration;

import com.aisys.library.audit.AuditService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class DataMigrationService {
    private final LegacyItemRepository repository;
    private final AuditService auditService;
    private final JdbcTemplate jdbcTemplate;
    private final ApplicationEventPublisher eventPublisher;

    public DataMigrationService(LegacyItemRepository repository, AuditService auditService, JdbcTemplate jdbcTemplate,
                                ApplicationEventPublisher eventPublisher) {
        this.repository = repository;
        this.auditService = auditService;
        this.jdbcTemplate = jdbcTemplate;
        this.eventPublisher = eventPublisher;
    }

    @Transactional
    public MigrationResult processMigration(InputStream csvStream, boolean isDryRun) {
        return processMigration(csvStream, isDryRun, "inline.csv", "SYSTEM");
    }

    @Transactional
    public MigrationResult processMigration(InputStream csvStream, boolean isDryRun, String filename, String actor) {
        MigrationResult result = new MigrationResult();
        Set<String> existingBarcodes = repository.findAll().stream()
                .map(LegacyItem::getBarcode)
                .collect(Collectors.toCollection(HashSet::new));

        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvStream))) {
            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                result.totalProcessed++;
                String[] columns = line.split(",", -1);

                if (columns.length < 4 || columns[0].trim().isEmpty() || columns[1].trim().isEmpty() || columns[2].trim().isEmpty()) {
                    result.invalidRecords++;
                    result.errorLogs.add("Row " + result.totalProcessed + ": Missing title, author, or barcode.");
                    continue;
                }

                String barcode = columns[2].trim();
                if (existingBarcodes.contains(barcode)) {
                    result.duplicateRecords++;
                    result.errorLogs.add("Row " + result.totalProcessed + ": Duplicate barcode " + barcode);
                    continue;
                }

                existingBarcodes.add(barcode);
                result.validRecords++;

                if (!isDryRun) {
                    repository.save(new LegacyItem(columns[0].trim(), columns[1].trim(), barcode, columns[3].trim()));
                }
            }

            jdbcTemplate.update(
                    "INSERT INTO migration_batches (filename, start_time, total_records, valid_records, duplicate_records, invalid_records, status) VALUES (?,?,?,?,?,?,?)",
                    filename,
                    Timestamp.from(Instant.now()),
                    result.totalProcessed,
                    result.validRecords,
                    result.duplicateRecords,
                    result.invalidRecords,
                    isDryRun ? "DRY_RUN" : "COMPLETED"
            );
            auditService.logAction(actor, "MIGRATION_UPLOAD", filename, isDryRun ? "DRY_RUN" : "SUCCESS");
            eventPublisher.publishEvent(new MigrationCompletedEvent(this, filename, isDryRun, actor, result));
        } catch (Exception e) {
            result.errorLogs.add("Migration failed: " + e.getMessage());
            auditService.logAction(actor, "MIGRATION_UPLOAD", filename, "FAILED");
            if (!isDryRun) {
                throw new RuntimeException("Migration aborted", e);
            }
        }

        return result;
    }
}
