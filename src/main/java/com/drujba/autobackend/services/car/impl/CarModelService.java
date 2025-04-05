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
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CarModelService implements ICarModelService {

    private final CarModelRepository carModelRepository;

    @Override
    @Transactional
    @Cacheable(value = "carModels", key = "{#carModelDto.brand, #carModelDto.model, #carModelDto.generation}")
    public UUID saveCarModel(CarModelDto carModelDto) {
        if (carModelRepository.existsByBrandAndModelAndGeneration(
                carModelDto.getBrand(),
                carModelDto.getModel(),
                carModelDto.getGeneration())) {
            throw new CarModelAlreadyExistException();
        }
        CarModel newCarModel = new CarModel(carModelDto);
        return carModelRepository.save(newCarModel).getId();
    }

    @Override
    @Transactional
    @CacheEvict(value = {"carModels", "carFilters"}, allEntries = true)
    public void deleteCarModel(CarModelDto carModelDto) {
        CarModel carModel = carModelRepository.findByBrandAndModelAndGeneration(
                        carModelDto.getBrand(),
                        carModelDto.getModel(),
                        carModelDto.getGeneration())
                .orElseThrow(() -> new CarModelDoesNotExistException(carModelDto.getModel()));

        log.info("Deleting car model: {}", carModel);
        carModelRepository.delete(carModel);
    }

    @Override
    @Transactional
    @CacheEvict(value = {"carModels", "carFilters"}, allEntries = true)
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
    @Cacheable(value = "carFilters")
    @Transactional(readOnly = true)
    public FilterDataDto getAllFilters() {
        // Cache the entire hierarchical structure to avoid multiple queries

        // Get all the data in a single query
        List<Object[]> allModelData = carModelRepository.findAllBrandsModelsGeneration();

        // Extract distinct brands
        List<String> brands = allModelData.stream()
                .map(arr -> (String) arr[0])
                .distinct()
                .collect(Collectors.toList());

        Map<String, List<String>> modelsByBrand = new HashMap<>();
        Map<String, List<String>> generationsByModel = new HashMap<>();

        // Group by brand
        Map<String, List<Object[]>> groupedByBrand = allModelData.stream()
                .collect(Collectors.groupingBy(
                        arr -> (String) arr[0],
                        Collectors.toList()
                ));

        // Process each brand
        for (String brand : brands) {
            List<Object[]> brandData = groupedByBrand.get(brand);

            // Extract models for this brand
            List<String> models = brandData.stream()
                    .map(arr -> (String) arr[1])
                    .distinct()
                    .collect(Collectors.toList());

            modelsByBrand.put(brand, models);

            // Group by model within the brand
            Map<String, List<Object[]>> groupedByModel = brandData.stream()
                    .collect(Collectors.groupingBy(
                            arr -> (String) arr[1],
                            Collectors.toList()
                    ));

            // Extract generations for each model
            for (String model : models) {
                List<String> generations = groupedByModel.get(model).stream()
                        .map(arr -> (String) arr[2])
                        .distinct()
                        .collect(Collectors.toList());

                generationsByModel.put(model, generations);
            }
        }

        return new FilterDataDto(brands, modelsByBrand, generationsByModel);
    }
}