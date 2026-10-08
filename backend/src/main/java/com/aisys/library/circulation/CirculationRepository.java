package com.aisys.library.circulation;

import com.aisys.library.catalog.Item;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CirculationRepository extends JpaRepository<CirculationTransaction, Long> {
    Optional<CirculationTransaction> findFirstByItemAndStatus(Item item, String status);
}
