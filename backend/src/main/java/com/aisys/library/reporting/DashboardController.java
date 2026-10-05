package com.aisys.library.reporting;

import com.aisys.library.catalog.ItemRepository;
import com.aisys.library.member.MemberRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/dashboard")
public class DashboardController {
    private final ItemRepository itemRepository;
    private final MemberRepository memberRepository;

    public DashboardController(ItemRepository itemRepository, MemberRepository memberRepository) {
        this.itemRepository = itemRepository;
        this.memberRepository = memberRepository;
    }

    @GetMapping("/stats")
    public Map<String, Long> getDashboardStats() {
        return Map.of(
            "totalItems", itemRepository.count(),
            "totalMembers", memberRepository.count()
        );
    }
}