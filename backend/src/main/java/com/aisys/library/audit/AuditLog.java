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
}