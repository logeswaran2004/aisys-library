package com.aisys.library.circulation;

import com.aisys.library.catalog.Item;
import com.aisys.library.member.Member;
import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "circulation_transactions")
public class CirculationTransaction {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "checkout_date")
    private Instant checkoutDate;

    @Column(name = "due_date")
    private Instant dueDate;

    @Column(name = "return_date")
    private Instant returnDate;

    private String status;

    public CirculationTransaction() {}

    public CirculationTransaction(Item item, Member member, Instant checkoutDate, Instant dueDate, String status) {
        this.item = item;
        this.member = member;
        this.checkoutDate = checkoutDate;
        this.dueDate = dueDate;
        this.status = status;
    }

    public Long getId() { return id; }
    public Item getItem() { return item; }
    public Member getMember() { return member; }
    public Instant getDueDate() { return dueDate; }
    public String getStatus() { return status; }
    
    public void setReturnDate(Instant returnDate) { this.returnDate = returnDate; }
    public void setStatus(String status) { this.status = status; }
    public void setDueDate(Instant dueDate) { this.dueDate = dueDate; }
}