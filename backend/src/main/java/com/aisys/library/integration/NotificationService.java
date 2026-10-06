package com.aisys.library.integration;
import org.springframework.stereotype.Component;

public interface NotificationService { void sendAlert(String message); }

@Component 
class MockNotificationService implements NotificationService {
    public void sendAlert(String message) { System.out.println("ALERT TRIGGERED: " + message); }
}