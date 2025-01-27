package com.drujba.autobackend.db.repositories;

import com.drujba.autobackend.db.entities.Report;
import com.drujba.autobackend.db.entities.auth.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID>, JpaSpecificationExecutor<Report> {
    Optional<Report> findFirstByCreatedAtBetween(Instant start, Instant end);

}