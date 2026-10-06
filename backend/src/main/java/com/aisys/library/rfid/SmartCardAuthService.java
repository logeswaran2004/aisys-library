package com.aisys.library.rfid;

import org.springframework.stereotype.Service;

@Service
public class SmartCardAuthService {
    public boolean authenticatePatron(String smartCardId) {
        // In a real system, this queries the database/LDAP for the patron
        return smartCardId != null && smartCardId.startsWith("SC-");
    }
}