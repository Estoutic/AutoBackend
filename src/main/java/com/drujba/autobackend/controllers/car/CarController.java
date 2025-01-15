package com.drujba.autobackend.controllers.car;

import com.drujba.autobackend.models.dto.auto.CarCreationDto;
import com.drujba.autobackend.services.car.impl.CarService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final CarService carService;

    @PostMapping()
    public ResponseEntity<UUID> addCar(@RequestBody CarCreationDto carCreationDto) {
        return ResponseEntity.ok(carService.saveCar(carCreationDto));
    }
}
