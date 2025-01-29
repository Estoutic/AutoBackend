package com.drujba.autobackend.controllers.branch;

import com.drujba.autobackend.models.dto.translation.BranchTranslationDto;
import com.drujba.autobackend.models.dto.translation.CarTranslationDto;
import com.drujba.autobackend.services.translation.ITranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/branch/translation")
@RequiredArgsConstructor
public class BranchTranslationController {

    private final ITranslationService translationService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping()
    public ResponseEntity<UUID> createBranchTranslation(@RequestBody BranchTranslationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(translationService.createBranchTranslation(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateBranchTranslation(@PathVariable UUID id, @RequestBody BranchTranslationDto dto) {
        translationService.updateBranchTranslation(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchTranslation(@PathVariable UUID id) {
        translationService.deleteBranchTranslation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{branchId}/all")
    public ResponseEntity<List<BranchTranslationDto>> getAllTranslations(@PathVariable UUID branchId) {
        return ResponseEntity.ok(translationService.getBranchTranslations(branchId));
    }
}
