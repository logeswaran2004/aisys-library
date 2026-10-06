package com.aisys.library.integration;
import org.springframework.stereotype.Component;

public interface CameraSubsystem { String captureGateImage(String gateId); }

@Component 
class MockCameraSubsystem implements CameraSubsystem {
    public String captureGateImage(String gateId) { 
        return "http://cctv.local/capture/" + gateId + "/" + System.currentTimeMillis() + ".jpg"; 
    }
}