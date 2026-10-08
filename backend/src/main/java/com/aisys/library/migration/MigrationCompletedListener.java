package com.aisys.library.migration;

import com.aisys.library.notification.NotificationProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class MigrationCompletedListener {
    private final NotificationProvider notificationProvider;

    @Value("${library.notification.ops-email:admin@library.local}")
    private String opsEmail;

    public MigrationCompletedListener(NotificationProvider notificationProvider) {
        this.notificationProvider = notificationProvider;
    }

    @EventListener
    public void onMigrationCompleted(MigrationCompletedEvent event) {
        MigrationResult result = event.getResult();
        String subject = event.isDryRun() ? "Migration dry-run completed" : "Migration job completed";
        String body = "File: " + event.getFilename()
                + "\nActor: " + event.getActor()
                + "\nDry run: " + event.isDryRun()
                + "\nTotal: " + result.totalProcessed
                + "\nValid: " + result.validRecords
                + "\nDuplicates: " + result.duplicateRecords
                + "\nInvalid: " + result.invalidRecords;
        notificationProvider.sendEmail(opsEmail, subject, body);
    }
}
