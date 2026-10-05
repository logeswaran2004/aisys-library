package com.aisys.library.member;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "members")
public class Member {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "member_id", unique = true, nullable = false)
    private String memberId;
    
    private String name;
    private String email;
    private String status;
    
    @Column(name = "fine_balance")
    private BigDecimal fineBalance;

    // Getters and Setters omitted for brevity but required by JPA
    public Long getId() { return id; }
    public String getMemberId() { return memberId; }
    public String getStatus() { return status; }
    public BigDecimal getFineBalance() { return fineBalance; }
}