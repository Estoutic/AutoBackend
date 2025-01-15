package com.drujba.autobackend.services.car.impl;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.car.CarModel;
import com.drujba.autobackend.db.repostiories.car.CarModelRepository;
import com.drujba.autobackend.db.repostiories.car.CarRepository;
import com.drujba.autobackend.exceptions.car.CarModelDoesNotExistException;
import com.drujba.autobackend.models.dto.auto.CarCreationDto;
import com.drujba.autobackend.services.car.ICarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarService implements ICarService {

    private final CarRepository carRepository;
    private final CarModelRepository carModelRepository;

    @Override
    public UUID saveCar(CarCreationDto carCreationDto) {
        CarModel carModel = carModelRepository.findById(carCreationDto.getCarId())
                .orElseThrow(() -> new CarModelDoesNotExistException(carCreationDto.getCarId().toString()));
        Car car = new Car(carCreationDto, carModel);
        return carRepository.save(car).getId();
    }
}
