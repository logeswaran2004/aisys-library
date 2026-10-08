package com.aisys.library.member;

import com.aisys.library.audit.AuditService;
import com.aisys.library.fine.Fine;
import com.aisys.library.fine.FineRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Service
public class FineService {
    private final MemberRepository memberRepository;
    private final FineRepository fineRepository;
    private final AuditService auditService;

    public FineService(MemberRepository memberRepository, FineRepository fineRepository, AuditService auditService) {
        this.memberRepository = memberRepository;
        this.fineRepository = fineRepository;
        this.auditService = auditService;
    }

    public List<Fine> listOutstanding() {
        return fineRepository.findByStatusOrderByCreatedAtAsc("OUTSTANDING");
    }

    public List<Fine> historyForMember(String memberId) {
        return fineRepository.findByMember_MemberIdOrderByCreatedAtDesc(memberId);
    }

    @Transactional
    public Fine addFine(String memberId, BigDecimal amount, String reason, Long circulationTransactionId, String operator) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Fine amount must be positive");
        }
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        Fine fine = new Fine(member, amount, reason, circulationTransactionId);
        Fine saved = fineRepository.save(fine);
        BigDecimal current = member.getFineBalance() != null ? member.getFineBalance() : BigDecimal.ZERO;
        member.setFineBalance(current.add(amount));
        memberRepository.save(member);
        auditService.logAction(operator, "FINE_ASSESSED", memberId, amount.toPlainString());
        return saved;
    }

    @Transactional
    public Member payFine(String memberId, BigDecimal amount, String operator) {
        Member member = memberRepository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));

        BigDecimal currentBalance = member.getFineBalance() != null ? member.getFineBalance() : BigDecimal.ZERO;
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new RuntimeException("Payment amount must be positive");
        }
        if (amount.compareTo(currentBalance) > 0) {
            throw new RuntimeException("Payment amount exceeds current fine balance.");
        }

        BigDecimal remaining = amount;
        List<Fine> outstanding = fineRepository.findByMember_MemberIdAndStatusOrderByCreatedAtAsc(memberId, "OUTSTANDING");
        for (Fine fine : outstanding) {
            if (remaining.compareTo(BigDecimal.ZERO) <= 0) {
                break;
            }
            if (remaining.compareTo(fine.getAmount()) >= 0) {
                remaining = remaining.subtract(fine.getAmount());
                fine.setStatus("PAID");
                fine.setPaidAt(Instant.now());
                fineRepository.save(fine);
            } else {
                Fine remainder = new Fine(member, fine.getAmount().subtract(remaining), fine.getReason(),
                        fine.getCirculationTransactionId());
                fine.setAmount(remaining);
                fine.setStatus("PAID");
                fine.setPaidAt(Instant.now());
                fineRepository.save(fine);
                fineRepository.save(remainder);
                remaining = BigDecimal.ZERO;
            }
        }

        member.setFineBalance(currentBalance.subtract(amount));
        Member saved = memberRepository.save(member);
        auditService.logAction(operator, "FINE_PAYMENT", memberId, "Paid: " + amount);
        return saved;
    }
}
