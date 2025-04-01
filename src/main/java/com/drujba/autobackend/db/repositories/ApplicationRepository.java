package com.drujba.autobackend.db.repositories;

import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.models.enums.application.ApplicationStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.UUID;

@Repository
public interface ApplicationRepository extends JpaRepository<Application, UUID> {

    // Existing methods
    Page<Application> findAllByOrderByCreatedAtDesc(Pageable pageable);
    Page<Application> findAllByStatusOrderByCreatedAtDesc(ApplicationStatus status, Pageable pageable);

    // New methods for date filtering

    // Filter by date range
    Page<Application> findAllByCreatedAtBetweenOrderByCreatedAtDesc(
            Instant createdAfter, Instant createdBefore, Pageable pageable);

    // Filter by status and date range
    Page<Application> findAllByStatusAndCreatedAtBetweenOrderByCreatedAtDesc(
            ApplicationStatus status, Instant createdAfter, Instant createdBefore, Pageable pageable);

    // Filter by status and date after
    Page<Application> findAllByStatusAndCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
            ApplicationStatus status, Instant createdAfter, Pageable pageable);

    // Filter by status and date before
    Page<Application> findAllByStatusAndCreatedAtLessThanEqualOrderByCreatedAtDesc(
            ApplicationStatus status, Instant createdBefore, Pageable pageable);

    // Filter by date after
    Page<Application> findAllByCreatedAtGreaterThanEqualOrderByCreatedAtDesc(
            Instant createdAfter, Pageable pageable);

    // Filter by date before
    Page<Application> findAllByCreatedAtLessThanEqualOrderByCreatedAtDesc(
            Instant createdBefore, Pageable pageable);
}