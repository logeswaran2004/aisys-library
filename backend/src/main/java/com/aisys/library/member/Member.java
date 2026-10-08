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

    public Long getId() { return id; }
    public String getMemberId() { return memberId; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public String getStatus() { return status; }
    public BigDecimal getFineBalance() { return fineBalance; }

    public void setId(Long id) { this.id = id; }
    public void setMemberId(String memberId) { this.memberId = memberId; }
    public void setName(String name) { this.name = name; }
    public void setEmail(String email) { this.email = email; }
    public void setStatus(String status) { this.status = status; }
    public void setFineBalance(BigDecimal fineBalance) { this.fineBalance = fineBalance; }
}