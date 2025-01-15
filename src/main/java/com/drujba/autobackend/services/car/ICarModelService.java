package com.drujba.autobackend.services.car;

import com.drujba.autobackend.models.dto.auto.CarModelDto;

import java.util.UUID;

public interface ICarModelService {

    UUID saveCarModel(CarModelDto carModelDto);

    void deleteCarModel(UUID uuid);

    void updateCarModel(UUID id, CarModelDto carModelDto);
}
