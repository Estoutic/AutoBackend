package com.drujba.autobackend.services.car;

import com.drujba.autobackend.models.dto.car.CarModelDto;
import com.drujba.autobackend.models.dto.car.FilterDataDto;

import java.util.UUID;

public interface ICarModelService {

    UUID saveCarModel(CarModelDto carModelDto);

    void deleteCarModel(CarModelDto carModelDto);

    void updateCarModel(UUID id, CarModelDto carModelDto);

    FilterDataDto getAllFilters();
}
