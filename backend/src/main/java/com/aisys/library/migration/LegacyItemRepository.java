package com.aisys.library.migration;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LegacyItemRepository extends JpaRepository<LegacyItem, Long> {}