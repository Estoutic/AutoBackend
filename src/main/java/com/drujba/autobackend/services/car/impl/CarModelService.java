package com.drujba.autobackend.services.car.impl;

import com.drujba.autobackend.db.entities.car.CarModel;
import com.drujba.autobackend.db.repostiories.car.CarModelRepository;
import com.drujba.autobackend.exceptions.car.CarModelAlreadyExistException;
import com.drujba.autobackend.exceptions.car.CarModelDoesNotExistException;
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

    @Override
    public void updateCarModel(UUID id, CarModelDto carModelDto) {
        CarModel carModel = carModelRepository.findById(id)
                .orElseThrow(() -> new CarModelDoesNotExistException(id.toString()));

        if (carModelDto.getBrand() != null) {
            carModel.setBrand(carModelDto.getBrand());
        }
        if (carModelDto.getModel() != null) {
            carModel.setModel(carModelDto.getModel());
        }
        if (carModelDto.getGeneration() != null) {
            carModel.setGeneration(carModelDto.getGeneration());
        }

        carModelRepository.save(carModel);
    }
}
