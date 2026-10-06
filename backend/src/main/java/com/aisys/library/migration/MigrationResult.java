package com.aisys.library.migration;

import java.util.ArrayList;
import java.util.List;

public class MigrationResult {
    public int totalProcessed = 0;
    public int validRecords = 0;
    public int duplicateRecords = 0;
    public int invalidRecords = 0;
    public List<String> errorLogs = new ArrayList<>();
}