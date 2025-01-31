package com.drujba.autobackend.controllers.branch;

import com.drujba.autobackend.models.dto.branch.BranchCreationDto;
import com.drujba.autobackend.models.dto.branch.BranchDto;
import com.drujba.autobackend.models.enums.Locale;
import com.drujba.autobackend.services.branch.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/branch")
@RequiredArgsConstructor
public class BranchController {

    private final IBranchService branchService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping
    public ResponseEntity<UUID> createBranch(@RequestBody BranchCreationDto branchCreationDTO) {
        return ResponseEntity.status(201).body(branchService.saveBranch(branchCreationDTO));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<BranchDto>> getAllBranches(@RequestParam(defaultValue = "EU") Locale locale) {
        return ResponseEntity.ok(branchService.getAllBranches(locale));
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDto> getBranchById(@PathVariable UUID id, @RequestParam(defaultValue = "EU") Locale locale) {
        return ResponseEntity.ok(branchService.getBranchById(id, locale));
    }
}