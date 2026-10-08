package com.aisys.library.circulation;

import com.aisys.library.catalog.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CirculationRepository extends JpaRepository<CirculationTransaction, Long> {
    Optional<CirculationTransaction> findFirstByItemAndStatus(Item item, String status);
    List<CirculationTransaction> findByMember_MemberId(String memberId);
    List<CirculationTransaction> findByMember_MemberIdAndStatus(String memberId, String status);
}
