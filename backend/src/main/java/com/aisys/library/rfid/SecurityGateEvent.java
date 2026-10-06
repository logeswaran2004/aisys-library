package com.aisys.library.rfid;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "security_gate_events")
public class SecurityGateEvent {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "gate_id")
    private String gateId;
    
    @Column(name = "rfid_tag_id")
    private String rfidTagId;
    
    @Column(name = "accession_number")
    private String accessionNumber;
    
    private Instant timestamp = Instant.now();
    private String status;
    
    @Column(name = "cctv_image_ref")
    private String cctvImageRef;

    public SecurityGateEvent() {}
    
    public SecurityGateEvent(String gateId, String rfidTagId, String accessionNumber, String status, String cctvImageRef) {
        this.gateId = gateId;
        this.rfidTagId = rfidTagId;
        this.accessionNumber = accessionNumber;
        this.status = status;
        this.cctvImageRef = cctvImageRef;
    }

    public String getRfidTagId() { return rfidTagId; }
    public Instant getTimestamp() { return timestamp; }
}