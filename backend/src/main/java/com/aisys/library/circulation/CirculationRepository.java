package com.aisys.library.circulation;
import org.springframework.data.jpa.repository.JpaRepository;
public interface CirculationRepository extends JpaRepository<CirculationTransaction, Long> {}