package com.drujba.autobackend.controllers.car;

import com.drujba.autobackend.db.repositories.car.CarModelRepository;
import com.drujba.autobackend.models.dto.car.CarModelDto;
import com.drujba.autobackend.services.car.ICarModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/car/model")
@RequiredArgsConstructor
public class CarModelController {

    private final ICarModelService carModelService;
    private final CarModelRepository carModelRepository;

    @PreAuthorize("hasAnyRole('MANAGER','ADMIN','SUPERADMIN')")
    @PostMapping("")
    public ResponseEntity<UUID> createModel(@RequestBody CarModelDto carModelDto) {
        return new ResponseEntity<>(carModelService.saveCarModel(carModelDto), HttpStatus.CREATED);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @DeleteMapping("/{id}")
    public void delete(@PathVariable(name = "id") UUID id) {
        carModelService.deleteCarModel(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateModel(@PathVariable UUID id, @RequestBody CarModelDto carModelDto) {
        carModelService.updateCarModel(id, carModelDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/brands")
    public ResponseEntity<List<String>> getBrands() {
        List<String> brands = carModelRepository.findDistinctBrands();
        return ResponseEntity.ok(brands);
    }

    @GetMapping("/models")
    public ResponseEntity<List<String>> getModels(@RequestParam String brand) {
        List<String> models = carModelRepository.findDistinctModelsByBrand(brand);
        return ResponseEntity.ok(models);
    }

    @GetMapping("/generations")
    public ResponseEntity<List<String>> getGenerations(@RequestParam String model) {
        List<String> generations = carModelRepository.findDistinctGenerationsByModel(model);
        return ResponseEntity.ok(generations);
    }
}
