package com.drujba.autobackend.controllers.admin;

import com.drujba.autobackend.models.dto.audit.AdminAuditLogDto;
import com.drujba.autobackend.services.audit.AdminAuditService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.Set;

@RestController
@RequestMapping("/admin/audit")
@RequiredArgsConstructor
//@PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
public class AdminAuditController {

    private final AdminAuditService auditService;

    @GetMapping("/logs")
    public ResponseEntity<Page<AdminAuditLogDto>> getAuditLogs(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "timestamp") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir) {

        // Validate sortDir
        Sort.Direction direction;
        try {
            direction = Sort.Direction.fromString(sortDir.toLowerCase());
        } catch (IllegalArgumentException e) {
            direction = Sort.Direction.DESC;
        }

        // Validate sortBy against a list of valid property names
        Set<String> validProperties = Set.of("id", "entityType", "actionType", "entityId", "performedBy", "timestamp");
        if (!validProperties.contains(sortBy)) {
            sortBy = "timestamp"; // Default to timestamp if invalid property is provided
        }

        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.ok(auditService.getAuditLogs(pageable));
    }

    @GetMapping("/logs/date-range")
    public ResponseEntity<Page<AdminAuditLogDto>> getAuditLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        return ResponseEntity.ok(auditService.getAuditLogsByDateRange(startDate, endDate, pageable));
    }

    @GetMapping("/logs/entity-type/{entityType}")
    public ResponseEntity<Page<AdminAuditLogDto>> getAuditLogsByEntityType(
            @PathVariable String entityType,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        return ResponseEntity.ok(auditService.getAuditLogsByEntityType(entityType, pageable));
    }

    @GetMapping("/logs/admin/{adminEmail}")
    public ResponseEntity<Page<AdminAuditLogDto>> getAuditLogsByAdmin(
            @PathVariable String adminEmail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "timestamp"));
        return ResponseEntity.ok(auditService.getAuditLogsByAdmin(adminEmail, pageable));
    }
}