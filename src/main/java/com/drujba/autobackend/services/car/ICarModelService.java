package com.drujba.autobackend.services.car;

import com.drujba.autobackend.db.entities.auto.CarModel;
import com.drujba.autobackend.models.dto.auto.CarModelDto;

import java.util.UUID;

public interface ICarModelService {

    UUID saveCarModel(CarModelDto carModelDto);
}
