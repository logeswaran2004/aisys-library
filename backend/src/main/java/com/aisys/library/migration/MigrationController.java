package com.aisys.library.migration;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/migration", "/api/migration"})
public class MigrationController {
    private final DataMigrationService migrationService;

    public MigrationController(DataMigrationService migrationService) {
        this.migrationService = migrationService;
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, Object>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam(name = "dryRun", defaultValue = "false") boolean dryRun,
            Principal principal) throws Exception {
        String actor = principal != null ? principal.getName() : "SYSTEM";
        MigrationResult result = migrationService.processMigration(file.getInputStream(), dryRun, file.getOriginalFilename(), actor);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("status", result.invalidRecords == 0 && result.duplicateRecords == 0 ? "success" : "completed_with_exceptions");
        response.put("filename", file.getOriginalFilename());
        response.put("dryRun", dryRun);
        response.put("totalRecordsProcessed", result.totalProcessed);
        response.put("validRecords", result.validRecords);
        response.put("duplicateRecords", result.duplicateRecords);
        response.put("invalidRecords", result.invalidRecords);
        response.put("errorLogs", result.errorLogs);
        return ResponseEntity.ok(response);
    }
}
