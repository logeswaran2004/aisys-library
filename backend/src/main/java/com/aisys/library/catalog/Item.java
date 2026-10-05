package com.aisys.library.catalog;

import jakarta.persistence.*;

@Entity
@Table(name = "items")
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "accession_number", unique = true, nullable = false)
    private String accessionNumber;
    
    @Column(unique = true)
    private String barcode;
    
    @ManyToOne
    @JoinColumn(name = "bibliographic_record_id", nullable = false)
    private BibliographicRecord bibliographicRecord;
    
    private String status; // e.g. AVAILABLE, ISSUED, MISSING

    public Long getId() { return id; }
    public String getAccessionNumber() { return accessionNumber; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public BibliographicRecord getBibliographicRecord() { return bibliographicRecord; }
}