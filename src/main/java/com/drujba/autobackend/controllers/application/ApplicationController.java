package com.drujba.autobackend.controllers.application;

import com.drujba.autobackend.db.entities.Application;
import com.drujba.autobackend.models.dto.apllication.ApplicationCreationDto;
import com.drujba.autobackend.models.dto.apllication.ApplicationDto;
import com.drujba.autobackend.models.enums.application.ApplicationStatus;
import com.drujba.autobackend.services.application.IApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteApplication(@PathVariable UUID id) {
        applicationService.deleteApplication(id);
        return ResponseEntity.noContent().build();
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateApplicationStatus(@PathVariable UUID id,
                                                        @RequestParam ApplicationStatus status) {
        applicationService.updateApplicationStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApplicationDto> getApplication(@PathVariable UUID id) {
        return ResponseEntity.ok(applicationService.getApplication(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<ApplicationDto>> getAllApplications(Pageable pageable) {
        return ResponseEntity.ok(applicationService.getApplications(pageable));
    }


}
