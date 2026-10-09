package com.aisys.library.rfid;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BackupService {
    
    private final JdbcTemplate jdbcTemplate;
    private static final String BACKUP_DIR = "./backup";

    // Inject Spring's JdbcTemplate to run native SQL commands
    public BackupService(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        new File(BACKUP_DIR).mkdirs();
    }

    public String createBackup() {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String fileName = "library_snapshot_" + timestamp + ".sql";
            File dest = new File(BACKUP_DIR, fileName);
            
            // Native H2 command to safely dump the live database state to a SQL file
            String absolutePath = dest.getAbsolutePath().replace("\\", "/");
            jdbcTemplate.execute("SCRIPT TO '" + absolutePath + "'");
            
            return "Database snapshot recorded successfully: " + fileName;
        } catch (Exception e) {
            throw new RuntimeException("Backup failed: " + e.getMessage());
        }
    }

    public String restoreLatest() {
        try {
            File dir = new File(BACKUP_DIR);
            File[] files = dir.listFiles((d, name) -> name.endsWith(".sql"));
            
            if (files == null || files.length == 0) {
                return "No valid backup files found.";
            }

            // Find the most recent snapshot
            File latest = files[0];
            for (File f : files) {
                if (f.lastModified() > latest.lastModified()) {
                    latest = f;
                }
            }

            // Instantly wipe the current state and restore the SQL snapshot
            String absolutePath = latest.getAbsolutePath().replace("\\", "/");
            jdbcTemplate.execute("DROP ALL OBJECTS");
            jdbcTemplate.execute("RUNSCRIPT FROM '" + absolutePath + "'");
            
            return "Restored successfully from backup snapshot: " + latest.getName();
        } catch (Exception e) {
            throw new RuntimeException("Restore failed: " + e.getMessage());
        }
    }
}