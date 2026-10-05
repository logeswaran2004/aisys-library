package com.aisys.library.rfid;
import org.springframework.context.ApplicationEvent;

public class GateAlarmEvent extends ApplicationEvent {
    private final String tagId;
    public GateAlarmEvent(Object source, String tagId) {
        super(source);
        this.tagId = tagId;
    }
    public String getTagId() { return tagId; }
}