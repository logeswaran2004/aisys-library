package com.aisys.library.circulation;

import com.aisys.library.audit.AuditService;
import com.aisys.library.catalog.Item;
import com.aisys.library.catalog.ItemRepository;
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

    @Value("${library.circulation.fine-limit:50.00}")
    private BigDecimal fineLimit;

    public CirculationService(CirculationRepository circulationRepository, ItemRepository itemRepository, MemberRepository memberRepository, AuditService auditService) {
        this.circulationRepository = circulationRepository;
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
        this.auditService = auditService;
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
        if (member.getFineBalance() != null && member.getFineBalance().compareTo(fineLimit) > 0) {
            throw new RuntimeException("AC04: Member fine exceeds configurable limit.");
        }
        if (!"AVAILABLE".equalsIgnoreCase(item.getStatus())) {
            throw new RuntimeException("Item is not available for checkout.");
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
}