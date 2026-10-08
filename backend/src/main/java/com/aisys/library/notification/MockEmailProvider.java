package com.aisys.library.notification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class MockEmailProvider implements NotificationProvider {
    private static final Logger log = LoggerFactory.getLogger(MockEmailProvider.class);
    
    @Override
    public void sendEmail(String to, String subject, String body) {
        log.info("MOCK EMAIL | To: {} | Subject: {} | Body: {}", to, subject, body);
    }
}
