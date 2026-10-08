package com.aisys.library.fine;

import com.aisys.library.member.Member;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "fines")
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false)
    private BigDecimal amount;

    private String reason;
    private String status = "OUTSTANDING";
    private Instant createdAt = Instant.now();
    private Instant paidAt;

    @Column(name = "circulation_transaction_id")
    private Long circulationTransactionId;

    public Fine() {}

    public Fine(Member member, BigDecimal amount, String reason, Long circulationTransactionId) {
        this.member = member;
        this.amount = amount;
        this.reason = reason;
        this.circulationTransactionId = circulationTransactionId;
        this.status = "OUTSTANDING";
        this.createdAt = Instant.now();
    }

    public Long getId() { return id; }
    public Member getMember() { return member; }
    public BigDecimal getAmount() { return amount; }
    public String getReason() { return reason; }
    public String getStatus() { return status; }
    public Instant getCreatedAt() { return createdAt; }
    public Instant getPaidAt() { return paidAt; }
    public Long getCirculationTransactionId() { return circulationTransactionId; }

    public void setId(Long id) { this.id = id; }
    public void setMember(Member member) { this.member = member; }
    public void setAmount(BigDecimal amount) { this.amount = amount; }
    public void setReason(String reason) { this.reason = reason; }
    public void setStatus(String status) { this.status = status; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setPaidAt(Instant paidAt) { this.paidAt = paidAt; }
    public void setCirculationTransactionId(Long circulationTransactionId) {
        this.circulationTransactionId = circulationTransactionId;
    }
}
