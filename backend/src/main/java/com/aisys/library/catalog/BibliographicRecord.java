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
    private String publisher;
    
    @Column(name = "is_reference")
    private Boolean isReference;

    public Long getId() { return id; }
    public String getIsbn() { return isbn; }
    public String getTitle() { return title; }
    public String getAuthor() { return author; }
    public String getPublisher() { return publisher; }
    public Boolean getIsReference() { return isReference; }

    public void setIsbn(String isbn) { this.isbn = isbn; }
    public void setTitle(String title) { this.title = title; }
    public void setAuthor(String author) { this.author = author; }
    public void setPublisher(String publisher) { this.publisher = publisher; }
    public void setId(Long id) { this.id = id; }
    public void setIsReference(Boolean isReference) { this.isReference = isReference; }
}