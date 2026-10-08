package com.aisys.library.circulation;

import com.aisys.library.audit.AuditService;
import com.aisys.library.catalog.Item;
import com.aisys.library.catalog.ItemRepository;
import com.aisys.library.ilms.LibrarySystemAdapter;
import com.aisys.library.integration.Sip2Adapter;
import com.aisys.library.member.Member;
import com.aisys.library.member.MemberRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

@Service
public class CirculationService {
    private final CirculationRepository circulationRepository;
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;
    private final AuditService auditService;
    private final LibrarySystemAdapter librarySystemAdapter;
    private final Sip2Adapter sip2Adapter;

    @Value("${library.circulation.fine-limit:50.00}")
    private BigDecimal fineLimit;

    public CirculationService(CirculationRepository circulationRepository, ItemRepository itemRepository,
                              MemberRepository memberRepository, AuditService auditService,
                              LibrarySystemAdapter librarySystemAdapter, Sip2Adapter sip2Adapter) {
        this.circulationRepository = circulationRepository;
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.auditService = auditService;
        this.librarySystemAdapter = librarySystemAdapter;
        this.sip2Adapter = sip2Adapter;
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
        Instant dueDate = now.plus(14, ChronoUnit.DAYS);

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
        item.setStatus("AVAILABLE");
        itemRepository.save(item);
        tx.setStatus("RETURNED");
        tx.setReturnDate(Instant.now());
        CirculationTransaction saved = circulationRepository.save(tx);
        auditService.logAction(operator, "CHECKIN", item.getAccessionNumber(), "SUCCESS");
        return saved;
    }
}
