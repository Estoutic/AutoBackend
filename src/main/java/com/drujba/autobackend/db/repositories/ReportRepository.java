package com.drujba.autobackend.db.repositories;

import com.drujba.autobackend.db.entities.Report;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ReportRepository extends JpaRepository<Report, UUID>, JpaSpecificationExecutor<Report> {
}