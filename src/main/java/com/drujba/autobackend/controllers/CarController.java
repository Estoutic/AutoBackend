package com.drujba.autobackend.controllers;

import com.drujba.autobackend.models.dto.auto.CarModelDto;
import com.drujba.autobackend.services.car.ICarModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/car")
@RequiredArgsConstructor
public class CarController {

    private final ICarModelService carModelService;

    @PostMapping("/model")
    public ResponseEntity<UUID> createModel(@RequestBody CarModelDto carModelDto) {
        return new ResponseEntity<>(carModelService.saveCarModel(carModelDto), HttpStatus.CREATED);
    }

}
