package com.aisys.library.member;

import com.aisys.library.audit.AuditService;
import org.springframework.stereotype.Service;
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
        Member saved = repository.save(member);
        auditService.logAction(actor, "CREATE_MEMBER", member.getMemberId(), "SUCCESS");
        return saved;
    }
}