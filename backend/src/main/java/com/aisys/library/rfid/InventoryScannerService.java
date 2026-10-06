package com.aisys.library.rfid;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class InventoryScannerService {
    public int processBatchTagging(List<String> rfidTags) {
        // Represents a librarian sweeping a wand over a shelf
        // Updates the "last seen" timestamp for all these tags
        if (rfidTags == null) return 0;
        return rfidTags.size();
    }
}