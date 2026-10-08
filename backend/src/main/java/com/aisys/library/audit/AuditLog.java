package com.aisys.library.audit;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String actor;
    private String action;
    private String resource;
    private Instant timestamp = Instant.now();
    private String result;
    
    @Column(name = "ip_address")
    private String ipAddress;

    public AuditLog() {}

    public AuditLog(String actor, String action, String resource, String result) {
        this.actor = actor;
        this.action = action;
        this.resource = resource;
        this.result = result;
    }

    public Long getId() { return id; }
    public String getActor() { return actor; }
    public String getAction() { return action; }
    public String getResource() { return resource; }
    public Instant getTimestamp() { return timestamp; }
    public String getResult() { return result; }
    public String getIpAddress() { return ipAddress; }

    public void setId(Long id) { this.id = id; }
    public void setActor(String actor) { this.actor = actor; }
    public void setAction(String action) { this.action = action; }
    public void setResource(String resource) { this.resource = resource; }
    public void setResult(String result) { this.result = result; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public void setIpAddress(String ipAddress) { this.ipAddress = ipAddress; }
}