package com.aisys.library.acquisition;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "acquisitions")
public class Acquisition {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String vendor;
    private String poNumber;
    private BigDecimal cost;
    private String status; // e.g., ORDERED, RECEIVED

    public String getTitle() { return title; }
    public String getStatus() { return status; }
}