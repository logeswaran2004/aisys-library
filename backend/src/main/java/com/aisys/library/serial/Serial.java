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

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getIssn() { return issn; }
    public String getFrequency() { return frequency; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setIssn(String issn) { this.issn = issn; }
    public void setFrequency(String frequency) { this.frequency = frequency; }
}