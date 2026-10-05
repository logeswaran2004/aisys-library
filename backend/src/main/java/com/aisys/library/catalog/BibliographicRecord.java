package com.aisys.library.catalog;

import jakarta.persistence.*;

@Entity
@Table(name = "bibliographic_records")
public class BibliographicRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private String isbn;
    private String title;
    private String author;
    
    @Column(name = "is_reference")
    private Boolean isReference;

    public Boolean getIsReference() { return isReference; }
}