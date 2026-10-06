package com.aisys.library.migration;

import jakarta.persistence.*;

@Entity
@Table(name = "legacy_items")
public class LegacyItem {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String author;
    private String barcode;
    private String isbn;

    public LegacyItem() {}

    public LegacyItem(String title, String author, String barcode, String isbn) {
        this.title = title;
        this.author = author;
        this.barcode = barcode;
        this.isbn = isbn;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getAuthor() { return author; }
    public void setAuthor(String author) { this.author = author; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
}