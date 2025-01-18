package com.drujba.autobackend.controllers.branch;

import com.drujba.autobackend.models.dto.branch.BranchCreationDto;
import com.drujba.autobackend.models.dto.branch.BranchDto;
import com.drujba.autobackend.services.branch.IBranchService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/branch")
@RequiredArgsConstructor
public class BranchController {

    private final IBranchService branchService;

    @PostMapping
    public ResponseEntity<UUID> createBranch(@RequestBody BranchCreationDto branchCreationDTO) {
        return ResponseEntity.status(201).body(branchService.createBranch(branchCreationDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranch(@PathVariable UUID id) {
        branchService.deleteBranch(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/all")
    public ResponseEntity<List<BranchDto>> getAllBranches() {
        return ResponseEntity.ok(branchService.getAllBranches());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BranchDto> getBranchById(@PathVariable UUID id) {
        return ResponseEntity.ok(branchService.getBranchById(id));
    }
}