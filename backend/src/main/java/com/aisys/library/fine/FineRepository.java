package com.aisys.library.fine;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FineRepository extends JpaRepository<Fine, Long> {
    List<Fine> findByStatusOrderByCreatedAtAsc(String status);
    List<Fine> findByMember_MemberIdOrderByCreatedAtDesc(String memberId);
    List<Fine> findByMember_MemberIdAndStatusOrderByCreatedAtAsc(String memberId, String status);
}
