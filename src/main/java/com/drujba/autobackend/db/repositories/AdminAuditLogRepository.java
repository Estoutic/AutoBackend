package com.drujba.autobackend.db.repositories;

import com.drujba.autobackend.db.entities.AdminAuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface AdminAuditLogRepository extends JpaRepository<AdminAuditLog, UUID> {
    Page<AdminAuditLog> findByTimestampBetweenOrderByTimestampDesc(Instant start, Instant end, Pageable pageable);
    Page<AdminAuditLog> findByEntityTypeOrderByTimestampDesc(String entityType, Pageable pageable);
    Page<AdminAuditLog> findByPerformedByOrderByTimestampDesc(String performedBy, Pageable pageable);
}