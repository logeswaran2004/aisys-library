package com.aisys.library.serial;
import jakarta.persistence.*;

@Entity
@Table(name = "serials")
public class Serial {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String issn;
    private String frequency; // e.g., MONTHLY, WEEKLY

    public String getTitle() { return title; }
}