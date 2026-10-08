package com.aisys.library.member;

import com.aisys.library.audit.AuditService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class MemberService {
    private final MemberRepository repository;
    private final AuditService auditService;

    public MemberService(MemberRepository repository, AuditService auditService) {
        this.repository = repository;
        this.auditService = auditService;
    }

    public List<Member> getAllMembers() {
        return repository.findAll();
    }

    public Optional<Member> getMemberById(String memberId) {
        return repository.findByMemberId(memberId);
    }

    public Member createMember(Member member, String actor) {
        if (member.getMemberId() == null || member.getMemberId().isBlank()) {
            throw new RuntimeException("memberId is required");
        }
        if (member.getFineBalance() == null) {
            member.setFineBalance(BigDecimal.ZERO);
        }
        if (member.getStatus() == null || member.getStatus().isBlank()) {
            member.setStatus("ACTIVE");
        }
        Member saved = repository.save(member);
        auditService.logAction(actor, "CREATE_MEMBER", member.getMemberId(), "SUCCESS");
        return saved;
    }

    @Transactional
    public Member updateMember(String memberId, Member incoming, String actor) {
        Member existing = repository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        if (incoming.getName() != null) existing.setName(incoming.getName());
        if (incoming.getEmail() != null) existing.setEmail(incoming.getEmail());
        if (incoming.getStatus() != null) existing.setStatus(incoming.getStatus());
        if (incoming.getFineBalance() != null) existing.setFineBalance(incoming.getFineBalance());
        Member saved = repository.save(existing);
        auditService.logAction(actor, "UPDATE_MEMBER", memberId, "SUCCESS");
        return saved;
    }

    @Transactional
    public void deleteMember(String memberId, String actor) {
        Member existing = repository.findByMemberId(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found"));
        repository.delete(existing);
        auditService.logAction(actor, "DELETE_MEMBER", memberId, "SUCCESS");
    }
}
