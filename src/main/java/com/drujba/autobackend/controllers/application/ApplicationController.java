package com.drujba.autobackend.controllers.application;

import com.drujba.autobackend.annotations.AuditLog;
import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.models.dto.apllication.ApplicationCreationDto;
import com.drujba.autobackend.models.dto.apllication.ApplicationDto;
import com.drujba.autobackend.models.enums.Locale;
import com.drujba.autobackend.models.enums.application.ApplicationStatus;
import com.drujba.autobackend.services.application.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/application")
@RequiredArgsConstructor
public class ApplicationController {

    private final IApplicationService applicationService;

    @PostMapping()
    public ResponseEntity<UUID> saveApplication(@RequestBody ApplicationCreationDto applicationCreationDto) {
        return ResponseEntity.ok(applicationService.saveApplication(applicationCreationDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{id}")
    @AuditLog(entityType = "Application", action = "DELETE")
    public ResponseEntity<Void> deleteApplication(@PathVariable UUID id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SUPERADMIN')")
    @PatchMapping("/{id}")
    @AuditLog(entityType = "Application", action = "UPDATE_STATUS")
    public ResponseEntity<Void> updateApplicationStatus(@PathVariable UUID id,
                                                        @RequestParam ApplicationStatus status) {
        applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SUPERADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> getApplication(@PathVariable UUID id, @RequestParam(defaultValue = "EU") Locale locale) {
        return ResponseEntity.ok(applicationService.getApplication(id, locale));
    }

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SUPERADMIN')")
    @GetMapping("/filter")
    public ResponseEntity<Page<ApplicationDto>> getApplicationsByFilters(
            @RequestParam(required = false) ApplicationStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdAfter,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant createdBefore,
            @RequestParam(defaultValue = "EU") Locale locale,
            Pageable pageable) {

        return ResponseEntity.ok(
                applicationService.getApplicationsByFilters(pageable, status, createdAfter, createdBefore, locale)
        );
    }


}
