package com.aisys.library.audit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    @Query("""
            SELECT a FROM AuditLog a
            WHERE (:actor IS NULL OR LOWER(a.actor) LIKE LOWER(CONCAT('%', :actor, '%')))
              AND (:action IS NULL OR LOWER(a.action) LIKE LOWER(CONCAT('%', :action, '%')))
              AND (:resource IS NULL OR LOWER(a.resource) LIKE LOWER(CONCAT('%', :resource, '%')))
            ORDER BY a.timestamp DESC
            """)
    List<AuditLog> search(@Param("actor") String actor,
                          @Param("action") String action,
                          @Param("resource") String resource);
}
