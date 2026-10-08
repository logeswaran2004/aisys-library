package com.aisys.library.migration;

import org.springframework.context.ApplicationEvent;

public class MigrationCompletedEvent extends ApplicationEvent {
    private final String filename;
    private final boolean dryRun;
    private final String actor;
    private final MigrationResult result;

    public MigrationCompletedEvent(Object source, String filename, boolean dryRun, String actor, MigrationResult result) {
        super(source);
        this.filename = filename;
        this.dryRun = dryRun;
        this.actor = actor;
        this.result = result;
    }

    public String getFilename() { return filename; }
    public boolean isDryRun() { return dryRun; }
    public String getActor() { return actor; }
    public MigrationResult getResult() { return result; }
}
