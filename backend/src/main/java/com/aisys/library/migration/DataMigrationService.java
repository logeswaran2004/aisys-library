package com.aisys.library.migration;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

@Service
public class DataMigrationService {
    
    @Autowired
    private LegacyItemRepository repository;

    @Transactional
    public MigrationResult processMigration(InputStream csvStream, boolean isDryRun) {
        MigrationResult result = new MigrationResult();
        Set<String> existingBarcodes = new HashSet<>(); 
        
        try (BufferedReader br = new BufferedReader(new InputStreamReader(csvStream))) {
            String line;
            boolean isHeader = true;
            
            while ((line = br.readLine()) != null) {
                if (isHeader) { isHeader = false; continue; }
                
                result.totalProcessed++;
                String[] columns = line.split(",", -1);
                
                // FIXED: Now correctly validates that Title, Author, and Barcode all exist
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
        } catch (Exception e) {
            e.printStackTrace(); 
            result.errorLogs.add("Migration failed: " + e.getMessage());
            if (!isDryRun) throw new RuntimeException("Migration aborted", e); 
        }
        
        return result;
    }
}