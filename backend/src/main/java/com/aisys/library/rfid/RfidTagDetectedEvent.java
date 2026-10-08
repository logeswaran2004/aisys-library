package com.aisys.library.rfid;

import org.springframework.context.ApplicationEvent;

public class RfidTagDetectedEvent extends ApplicationEvent {
    private final String tagId;
    private final String source;

    public RfidTagDetectedEvent(Object source, String tagId, String origin) {
        super(source);
        this.tagId = tagId;
        this.source = origin;
    }

    public String getTagId() { return tagId; }
    public String getOrigin() { return source; }
}
