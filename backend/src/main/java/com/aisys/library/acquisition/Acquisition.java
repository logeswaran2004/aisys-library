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

    public Long getId() { return id; }
    public String getTitle() { return title; }
    public String getVendor() { return vendor; }
    public String getPoNumber() { return poNumber; }
    public BigDecimal getCost() { return cost; }
    public String getStatus() { return status; }

    public void setId(Long id) { this.id = id; }
    public void setTitle(String title) { this.title = title; }
    public void setVendor(String vendor) { this.vendor = vendor; }
    public void setPoNumber(String poNumber) { this.poNumber = poNumber; }
    public void setCost(BigDecimal cost) { this.cost = cost; }
    public void setStatus(String status) { this.status = status; }
}