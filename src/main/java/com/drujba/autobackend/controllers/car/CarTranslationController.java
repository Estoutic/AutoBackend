package com.drujba.autobackend.controllers.car;


import com.drujba.autobackend.annotations.AuditLog;
import com.drujba.autobackend.models.dto.translation.CarTranslationDto;
import com.drujba.autobackend.services.translation.impl.TranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/car/translation")
@RequiredArgsConstructor
public class CarTranslationController {

    private final TranslationService translationService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping()
    @AuditLog(entityType = "CarTranslation", action = "CREATE")
    public ResponseEntity<UUID> createCarTranslation(@RequestBody CarTranslationDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(translationService.createCarTranslation(dto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{id}")
    @AuditLog(entityType = "CarTranslation", action = "UPDATE")
    public ResponseEntity<Void> updateCarTranslation(@PathVariable UUID id, @RequestBody CarTranslationDto dto) {
        translationService.updateCarTranslation(id, dto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{id}")
    @AuditLog(entityType = "CarTranslation", action = "DELETE")
    public ResponseEntity<Void> deleteCarTranslation(@PathVariable UUID id) {
        translationService.deleteCarTranslation(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{carId}/all")
    public ResponseEntity<List<CarTranslationDto>> getAllTranslations(@PathVariable UUID carId) {
        return ResponseEntity.ok(translationService.getCarTranslations(carId));
    }

}
