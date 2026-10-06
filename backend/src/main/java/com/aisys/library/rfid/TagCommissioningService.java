package com.aisys.library.rfid;

import org.springframework.stereotype.Service;

@Service
public class TagCommissioningService {
    public boolean encodeNewTag(String accessionNumber, String newRfidTagId) {
        // Simulates writing library data onto a blank physical RFID tag
        return accessionNumber != null && newRfidTagId != null;
    }
}