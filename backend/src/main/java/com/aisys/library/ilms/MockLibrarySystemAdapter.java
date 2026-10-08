package com.aisys.library.ilms;

import com.aisys.library.member.Member;
import com.aisys.library.member.MemberRepository;
import org.springframework.stereotype.Component;

@Component
public class MockLibrarySystemAdapter implements LibrarySystemAdapter {
    private final MemberRepository memberRepository;

    public MockLibrarySystemAdapter(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public boolean verifyMemberStanding(String memberId) {
        return memberRepository.findByMemberId(memberId)
                .map(member -> !"BLOCKED".equalsIgnoreCase(member.getStatus()))
                .orElse(false);
    }
}
