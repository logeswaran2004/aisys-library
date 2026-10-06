package com.aisys.library.rfid;

import org.springframework.stereotype.Service;

@Service
public class RfidNotificationService {
    public boolean triggerSecurityAlert(String gateId, String accessionNumber) {
        // In reality, this would send an SMS/Email to the librarian or sound an alarm
        System.out.println("SECURITY ALERT: Unauthorized item " + accessionNumber + " detected at gate " + gateId);
        return true;
    }
}