package com.aisys.library.catalog;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface BibliographicRecordRepository extends JpaRepository<BibliographicRecord, Long> {
    List<BibliographicRecord> findByTitleContainingIgnoreCaseOrAuthorContainingIgnoreCase(String title, String author);
}