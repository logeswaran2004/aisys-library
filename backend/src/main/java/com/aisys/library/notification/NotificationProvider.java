package com.aisys.library.notification;
public interface NotificationProvider {
    void sendEmail(String to, String subject, String body);
}