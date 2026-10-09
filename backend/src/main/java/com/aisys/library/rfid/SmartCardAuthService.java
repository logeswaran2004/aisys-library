package com.aisys.library.rfid;

import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class SmartCardAuthService {
    
    // Define an allowed list of valid staff smart cards
    private static final List<String> VALID_CARDS = List.of("SC-1001", "SC-1002", "SC-1003");

    public boolean authenticatePatron(String smartCardId) {
        // Only return true if the card is explicitly in our system
        return smartCardId != null && VALID_CARDS.contains(smartCardId);
    }
}