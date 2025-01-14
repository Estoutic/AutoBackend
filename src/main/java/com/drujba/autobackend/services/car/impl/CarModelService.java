package com.drujba.autobackend.services.car.impl;

import com.drujba.autobackend.db.entities.auto.CarModel;
import com.drujba.autobackend.db.repostiories.car.CarModelRepository;
import com.drujba.autobackend.exceptions.car.CarModelAlreadyExistException;
import com.drujba.autobackend.models.dto.auto.CarModelDto;
import com.drujba.autobackend.services.car.ICarModelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarModelService implements ICarModelService {

    private final CarModelRepository carModelRepository;

    @Override
    public UUID saveCarModel(CarModelDto carModelDto) {
        if (carModelRepository.existsByBrandAndModelAndGeneration(carModelDto.getBrand(), carModelDto.getModel(), carModelDto.getGeneration())) {
            throw new CarModelAlreadyExistException();
        }
        CarModel newCarModel = new CarModel(carModelDto);

        return carModelRepository.save(newCarModel).getId();
    }

    @Override
    public void deleteCarModel(UUID uuid) {
        if (carModelRepository.existsById(uuid)) {
            carModelRepository.deleteById(uuid);
        }
    }
}
