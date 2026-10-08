package com.aisys.library.rfid;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/backup", "/api/backup"})
public class BackupController {
    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @PostMapping("/create")
    public ResponseEntity<Map<String, String>> createBackup() {
        String result = backupService.createBackup();
        return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", result));
    }

    @PostMapping("/restore")
    public ResponseEntity<Map<String, String>> restoreBackup() {
        String result = backupService.restoreLatest();
        return ResponseEntity.ok(Map.of("status", "SUCCESS", "message", result));
    }
}