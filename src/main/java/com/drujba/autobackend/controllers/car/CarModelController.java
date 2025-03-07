package com.drujba.autobackend.controllers.car;

import com.drujba.autobackend.db.entities.car.CarModel;
import com.drujba.autobackend.db.repositories.car.CarModelRepository;
import com.drujba.autobackend.exceptions.car.CarModelDoesNotExistException;
import com.drujba.autobackend.models.dto.car.CarModelDto;
import com.drujba.autobackend.models.dto.car.FilterDataDto;
import com.drujba.autobackend.services.car.ICarModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Slf4j
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
    @DeleteMapping(produces = MediaType.APPLICATION_JSON_VALUE, params = {"brand", "model", "generation"})
    public ResponseEntity<Void> delete(
            @RequestParam String brand,
            @RequestParam String model,
            @RequestParam String generation
    ) {
        CarModelDto carModelDto = new CarModelDto();
        carModelDto.setBrand(brand);
        carModelDto.setModel(model);
        carModelDto.setGeneration(generation);

        log.info("Deleting car model: {}", carModelDto);


        carModelService.deleteCarModel(carModelDto);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateModel(@PathVariable UUID id, @RequestBody CarModelDto carModelDto) {
        carModelService.updateCarModel(id, carModelDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("")
    public ResponseEntity<CarModelDto> getCarModel(@RequestBody CarModelDto carModelDto) {
        CarModel carModel = carModelRepository.findByBrandAndModelAndGeneration(carModelDto.getBrand(),carModelDto.getModel(),
                carModelDto.getGeneration()).orElseThrow(() -> new CarModelDoesNotExistException(carModelDto.getModel()));
        return ResponseEntity.ok(new CarModelDto(carModel));
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
    @GetMapping("/allFilters")
    public ResponseEntity<FilterDataDto> getAllFilters() {
        return new ResponseEntity<FilterDataDto>(carModelService.getAllFilters(), HttpStatus.OK);
    }
}
