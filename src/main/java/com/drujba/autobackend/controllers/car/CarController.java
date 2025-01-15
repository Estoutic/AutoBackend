package com.drujba.autobackend.controllers.car;

import com.drujba.autobackend.models.dto.car.CarCreationDto;
import com.drujba.autobackend.models.dto.car.CarDto;
import com.drujba.autobackend.models.dto.car.CarUpdateDto;
import com.drujba.autobackend.services.car.ICarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final ICarService carService;

    @PostMapping()
    public ResponseEntity<UUID> addCar(@RequestBody CarCreationDto carCreationDto) {
        return ResponseEntity.ok(carService.saveCar(carCreationDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.deleteCar(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateCar(@PathVariable UUID id, @RequestBody CarUpdateDto carUpdateDto){
        carService.updateCar(id, carUpdateDto);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CarDto> getCar(@PathVariable UUID id) {
        return ResponseEntity.ok(carService.getCar(id));
    }
}
