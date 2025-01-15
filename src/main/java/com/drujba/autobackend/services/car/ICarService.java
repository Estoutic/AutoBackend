package com.drujba.autobackend.services.car;

import com.drujba.autobackend.models.dto.car.CarCreationDto;
import com.drujba.autobackend.models.dto.car.CarUpdateDto;

import java.util.UUID;

public interface ICarService {

    UUID saveCar(CarCreationDto carCreationDto);

    void deleteCar(UUID id);

    void updateCar(UUID id, CarUpdateDto carUpdateDto);
}
