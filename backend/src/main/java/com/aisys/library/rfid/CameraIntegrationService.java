package com.aisys.library.rfid;

import org.springframework.stereotype.Service;
import java.util.UUID;

@Service
public class CameraIntegrationService {
    public String captureSnapshot(String gateId) {
        // Simulates triggering an IP camera and returning the image reference URL
        return "cctv://archive/" + gateId + "/" + UUID.randomUUID().toString() + ".jpg";
    }
}