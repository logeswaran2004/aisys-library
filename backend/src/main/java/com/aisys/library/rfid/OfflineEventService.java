package com.aisys.library.rfid;

import org.springframework.stereotype.Service;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class OfflineEventService {
    private final Queue<SecurityGateEvent> offlineQueue = new ConcurrentLinkedQueue<>();

    public void queueEventForRetry(SecurityGateEvent event) {
        offlineQueue.offer(event);
    }

    public int getPendingEventCount() {
        return offlineQueue.size();
    }

    public boolean processRetry() {
        // Simulating the system coming back online and processing the backlog
        SecurityGateEvent event = offlineQueue.poll();
        return event != null;
    }
}