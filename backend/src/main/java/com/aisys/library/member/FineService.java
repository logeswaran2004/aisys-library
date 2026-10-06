package com.aisys.library.member;

import com.aisys.library.audit.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
public class FineService {
    private final MemberRepository memberRepository;
    private final AuditService auditService;

    public FineService(MemberRepository memberRepository, AuditService auditService) {
        this.memberRepository = memberRepository;
        this.auditService = auditService;
    }

    @Transactional
    public Member payFine(String memberId, BigDecimal amount, String operator) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        
        BigDecimal currentBalance = member.getFineBalance() != null ? member.getFineBalance() : BigDecimal.ZERO;
        if (amount.compareTo(currentBalance) > 0) {
            throw new RuntimeException("Payment amount exceeds current fine balance.");
        }
        
        member.setFineBalance(currentBalance.subtract(amount));
        Member saved = memberRepository.save(member);
        
        auditService.logAction(operator, "FINE_PAYMENT", memberId, "Paid: " + amount);
        return saved;
    }
}