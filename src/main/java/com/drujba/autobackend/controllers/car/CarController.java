package com.drujba.autobackend.controllers.car;

import com.drujba.autobackend.models.dto.car.CarCreationDto;
import com.drujba.autobackend.models.dto.car.CarDto;
import com.drujba.autobackend.models.dto.car.CarFilterDto;
import com.drujba.autobackend.models.dto.car.CarUpdateDto;
import com.drujba.autobackend.models.dto.translation.CarTranslationDto;
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
    private final ITranslationService  translationService;

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PostMapping()
    public ResponseEntity<UUID> addCar(@RequestBody CarCreationDto carCreationDto) {
        return ResponseEntity.ok(carService.saveCar(carCreationDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCar(@PathVariable UUID id, @RequestBody CarUpdateDto carUpdateDto){
        carService.updateCar(id, carUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCar(@PathVariable UUID id) {
        return ResponseEntity.ok(carService.getCar(id));
    }

    @GetMapping("/all")
    public ResponseEntity<Page<CarDto>> getAllCars(
            @RequestBody CarFilterDto filterDto,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortOrder) {

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(sortOrder), sortBy));
        Page<CarDto> cars = carService.getFilteredCars(filterDto, pageable);
        return ResponseEntity.ok(cars);
    }
}
