package com.aisys.library.rfid;

public interface DeviceEventHandler {
    void handleEvent(SecurityGateEvent event);
    boolean supports(String deviceType);
}