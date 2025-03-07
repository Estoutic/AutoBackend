package com.drujba.autobackend.services.car.impl;

import com.drujba.autobackend.db.entities.car.CarModel;
import com.drujba.autobackend.db.repositories.car.CarModelRepository;
import com.drujba.autobackend.exceptions.car.CarModelAlreadyExistException;
import com.drujba.autobackend.exceptions.car.CarModelDoesNotExistException;
import com.drujba.autobackend.models.dto.car.CarModelDto;
import com.drujba.autobackend.models.dto.car.FilterDataDto;
import com.drujba.autobackend.services.car.ICarModelService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
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
    public void deleteCarModel(CarModelDto carModelDto) {
        CarModel carModel = carModelRepository.findByBrandAndModelAndGeneration(carModelDto.getBrand(),
                carModelDto.getModel(), carModelDto.getGeneration()).orElseThrow(() -> new CarModelDoesNotExistException(carModelDto.getModel()));
        log.info("Deleting car model: " + carModel);
        carModelRepository.delete(carModel);
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

    @Override
    public FilterDataDto getAllFilters() {
        List<String> brands = carModelRepository.findDistinctBrands();
        Map<String, List<String>> modelsByBrand = new HashMap<>();
        Map<String, List<String>> generationsByModel = new HashMap<>();

        for (String brand : brands) {
            List<String> models = carModelRepository.findDistinctModelsByBrand(brand);
            modelsByBrand.put(brand, models);
            for (String model : models) {
                if (!generationsByModel.containsKey(model)) {
                    List<String> generations = carModelRepository.findDistinctGenerationsByModel(model);
                    generationsByModel.put(model, generations);
                }
            }
        }

        return (new FilterDataDto(brands, modelsByBrand, generationsByModel));
    }


}
