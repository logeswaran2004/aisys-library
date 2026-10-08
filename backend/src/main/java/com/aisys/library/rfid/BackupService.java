package com.aisys.library.rfid;

import org.springframework.stereotype.Service;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class BackupService {
    private static final String BACKUP_DIR = "./backup";

    private File findDatabaseFile() {
        // Search current directory and subdirectories for H2 database files
        File root = new File(".");
        return searchForDbFile(root);
    }

    private File searchForDbFile(File dir) {
        File[] files = dir.listFiles();
        if (files != null) {
            for (File f : files) {
                if (f.isDirectory() && !f.getName().equals("backup") && !f.getName().equals(".git") && !f.getName().equals("target")) {
                    File found = searchForDbFile(f);
                    if (found != null) return found;
                } else if (f.getName().endsWith(".mv.db")) {
                    return f;
                }
            }
        }
        return null;
    }

    public String createBackup() {
        try {
            File src = findDatabaseFile();
            if (src == null || !src.exists()) {
                return "Error: Could not locate active database file on disk.";
            }

            File dir = new File(BACKUP_DIR);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            File dest = new File(dir, "library_backup_" + timestamp + ".mv.db");
            
            Files.copy(src.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return "Backup created successfully from file: " + src.getName() + " -> " + dest.getName();
        } catch (IOException e) {
            throw new RuntimeException("Backup failed: " + e.getMessage());
        }
    }

    public String restoreLatest() {
        try {
            File dir = new File(BACKUP_DIR);
            if (!dir.exists() || dir.listFiles() == null) {
                return "No backup snapshots found to restore.";
            }
            File latest = null;
            for (File f : dir.listFiles()) {
                if (f.getName().endsWith(".mv.db")) {
                    if (latest == null || f.lastModified() > latest.lastModified()) {
                        latest = f;
                    }
                }
            }
            if (latest != null) {
                File dest = findDatabaseFile();
                if (dest == null) {
                    dest = new File("./library.mv.db");
                }
                Files.copy(latest.toPath(), dest.toPath(), StandardCopyOption.REPLACE_EXISTING);
                return "Restored successfully from backup snapshot: " + latest.getName();
            }
            return "No valid backup files found.";
        } catch (IOException e) {
            throw new RuntimeException("Restore failed: " + e.getMessage());
        }
    }
}