package com.drujba.autobackend.controllers.car;

import com.drujba.autobackend.annotations.AuditLog;
import com.drujba.autobackend.models.dto.car.*;
import com.drujba.autobackend.models.dto.translation.CarTranslationDto;
import com.drujba.autobackend.models.enums.Locale;
import com.drujba.autobackend.services.car.ICarService;
import com.drujba.autobackend.services.translation.ITranslationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final ICarService carService;
    private final ITranslationService translationService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping()
    @AuditLog(entityType = "Car", action = "CREATE")
    public ResponseEntity<UUID> addCar(@RequestBody CarCreationDto carCreationDto) {
        return ResponseEntity.ok(carService.saveCar(carCreationDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{id}")
    @AuditLog(entityType = "Car", action = "DELETE")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.hideCar(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{id}")
    @AuditLog(entityType = "Car", action = "UPDATE")
    public ResponseEntity<Void> updateCar(@PathVariable UUID id, @RequestBody CarUpdateDto carUpdateDto) {
        carService.updateCar(id, carUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarResponseDto> getCar(@PathVariable UUID id, @RequestParam(defaultValue = "EU") Locale locale) {
        return ResponseEntity.ok(carService.getCar(id,locale));
    }

    @PostMapping ("/all")
    public ResponseEntity<Page<CarResponseDto>> getAllCars(
            @RequestBody CarFilterDto filterDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder,
            @RequestParam(defaultValue = "EU") Locale locale) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        Page<CarResponseDto> cars = carService.getFilteredCars(filterDto, pageable,locale);
        return ResponseEntity.ok(cars);
    }
}
