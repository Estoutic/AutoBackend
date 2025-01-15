package com.drujba.autobackend.services.car;

import com.drujba.autobackend.models.dto.auto.CarCreationDto;

import java.util.UUID;

public interface ICarService {

    UUID saveCar(CarCreationDto carCreationDto);
}
