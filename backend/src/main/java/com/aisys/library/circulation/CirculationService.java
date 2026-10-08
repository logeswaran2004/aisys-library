package com.aisys.library.circulation;

import com.aisys.library.audit.AuditService;
import com.aisys.library.catalog.Item;
import com.aisys.library.catalog.ItemRepository;
import com.aisys.library.ilms.LibrarySystemAdapter;
import com.aisys.library.integration.Sip2Adapter;
import com.aisys.library.member.FineService;
import com.aisys.library.member.Member;
import com.aisys.library.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class CirculationService {
    private final CirculationRepository circulationRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final AuditService auditService;
    private final LibrarySystemAdapter librarySystemAdapter;
    private final Sip2Adapter sip2Adapter;
    private final FineService fineService;

    @Value("${library.circulation.fine-limit:50.00}")
    private BigDecimal fineLimit;

    @Value("${library.circulation.daily-fine-rate:1.00}")
    private BigDecimal dailyFineRate;

    @Value("${library.circulation.loan-days:14}")
    private int loanDays;

    public CirculationService(CirculationRepository circulationRepository, ItemRepository itemRepository,
                              MemberRepository memberRepository, AuditService auditService,
                              LibrarySystemAdapter librarySystemAdapter, Sip2Adapter sip2Adapter,
                              FineService fineService) {
        this.circulationRepository = circulationRepository;
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.auditService = auditService;
        this.librarySystemAdapter = librarySystemAdapter;
        this.sip2Adapter = sip2Adapter;
        this.fineService = fineService;
    }

    public List<CirculationTransaction> list(String memberId, String status) {
        if (memberId != null && !memberId.isBlank() && status != null && !status.isBlank()) {
            return circulationRepository.findByMember_MemberIdAndStatus(memberId, status);
        }
        if (memberId != null && !memberId.isBlank()) {
            return circulationRepository.findByMember_MemberId(memberId);
        }
        if (status != null && !status.isBlank()) {
            return circulationRepository.findAll().stream()
                    .filter(tx -> status.equalsIgnoreCase(tx.getStatus()))
                    .toList();
        }
        return circulationRepository.findAll();
    }

    public CirculationTransaction get(Long id) {
        return circulationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Circulation transaction not found"));
    }

    @Transactional
    public CirculationTransaction checkout(String barcode, String memberId, String operator) {
        Item item = itemRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        if (Boolean.TRUE.equals(item.getBibliographicRecord().getIsReference())) {
            throw new RuntimeException("AC04: Cannot checkout reference books.");
        }
        if ("BLOCKED".equalsIgnoreCase(member.getStatus())) {
            throw new RuntimeException("AC04: Member account is blocked.");
        }
        if (!librarySystemAdapter.verifyMemberStanding(memberId)) {
            throw new RuntimeException("AC04: Mock ILMS (NCIP) rejected member standing.");
        }
        if (member.getFineBalance() != null && member.getFineBalance().compareTo(fineLimit) > 0) {
            throw new RuntimeException("AC04: Member fine exceeds configurable limit.");
        }
        if (!"AVAILABLE".equalsIgnoreCase(item.getStatus())) {
            throw new RuntimeException("Item is not available for checkout.");
        }
        if (!sip2Adapter.checkoutItem(memberId, barcode)) {
            throw new RuntimeException("SIP2 checkout was rejected by mock ILMS.");
        }

        item.setStatus("ISSUED");
        itemRepository.save(item);

        Instant now = Instant.now();
        Instant dueDate = now.plus(loanDays, ChronoUnit.DAYS);

        CirculationTransaction tx = new CirculationTransaction(item, member, now, dueDate, "ACTIVE");
        CirculationTransaction saved = circulationRepository.save(tx);

        auditService.logAction(operator, "CHECKOUT", item.getAccessionNumber(), "SUCCESS");
        return saved;
    }

    @Transactional
    public CirculationTransaction checkin(String barcode, String operator) {
        Item item = itemRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        CirculationTransaction tx = circulationRepository.findFirstByItemAndStatus(item, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("No active loan found for item."));
        sip2Adapter.checkinItem(barcode);
        Instant returnDate = Instant.now();
        item.setStatus("AVAILABLE");
        itemRepository.save(item);
        tx.setStatus("RETURNED");
        tx.setReturnDate(returnDate);
        if (tx.getDueDate() != null && returnDate.isAfter(tx.getDueDate())) {
            long overdueDays = ChronoUnit.DAYS.between(tx.getDueDate(), returnDate);
            if (overdueDays < 1) {
                overdueDays = 1;
            }
            BigDecimal amount = dailyFineRate.multiply(BigDecimal.valueOf(overdueDays));
            fineService.addFine(tx.getMember().getMemberId(), amount,
                    "Overdue return of " + item.getAccessionNumber() + " (" + overdueDays + " day(s))",
                    tx.getId(), operator);
        }
        CirculationTransaction saved = circulationRepository.save(tx);
        auditService.logAction(operator, "CHECKIN", item.getAccessionNumber(), "SUCCESS");
        return saved;
    }

    @Transactional
    public CirculationTransaction renew(String barcode, String operator) {
        Item item = itemRepository.findByBarcode(barcode)
                .orElseThrow(() -> new RuntimeException("Item not found"));
        CirculationTransaction tx = circulationRepository.findFirstByItemAndStatus(item, "ACTIVE")
                .orElseThrow(() -> new RuntimeException("No active loan found for item."));
        Instant now = Instant.now();
        if (tx.getDueDate() != null && now.isAfter(tx.getDueDate())) {
            throw new RuntimeException("Cannot renew an overdue loan. Return the item and settle fines first.");
        }
        Instant newDue = (tx.getDueDate() != null ? tx.getDueDate() : now).plus(loanDays, ChronoUnit.DAYS);
        tx.setDueDate(newDue);
        CirculationTransaction saved = circulationRepository.save(tx);
        auditService.logAction(operator, "RENEW", item.getAccessionNumber(), "SUCCESS");
        return saved;
    }
}
