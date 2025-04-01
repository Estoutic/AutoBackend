package com.drujba.autobackend.services.translation.impl;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.translation.CarTranslation;
import com.drujba.autobackend.db.repositories.car.CarRepository;
import com.drujba.autobackend.db.repositories.translation.CarTranslationRepository;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.exceptions.car.CarTranslationAlreadyExistException;
import com.drujba.autobackend.exceptions.car.CarTranslationDoesNotExistException;
import com.drujba.autobackend.models.dto.translation.CarTranslationDto;
import com.drujba.autobackend.services.conversion.LocaleConversionService;
import com.drujba.autobackend.services.translation.ITranslationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TranslationService implements ITranslationService {

    private final CarRepository carRepository;
    private final CarTranslationRepository carTranslationRepository;
    private final LocaleConversionService conversionService;

    @Override
    @Transactional
    public UUID createCarTranslation(CarTranslationDto dto) {
        Car car = carRepository.findById(dto.getCarId())
                .orElseThrow(() -> new CarDoesNotExistException(dto.getCarId().toString()));

        if (carTranslationRepository.existsCarTranslationByLocale(dto.getLocale())) {
            throw new CarTranslationAlreadyExistException(car.getId(), dto.getLocale());
        }

        // Apply locale-specific conversions before saving
        applyLocaleConversions(dto);

        // Set currency code and miles flag if not explicitly provided
        if (dto.getCurrencyCode() == null) {
            dto.setCurrencyCode(conversionService.getCurrencyCode(dto.getLocale()));
        }

        // Only set isMiles if not explicitly set by client
        if (!dto.isMiles()) {
            dto.setMiles(conversionService.isMiles(dto.getLocale()));
        }

        CarTranslation carTranslation = new CarTranslation(dto, car);
        carTranslationRepository.save(carTranslation);
        return carTranslation.getId();
    }

    @Override
    @Transactional
    public void updateCarTranslation(UUID translationId, CarTranslationDto dto) {
        CarTranslation existingTranslation = carTranslationRepository.findById(translationId)
                .orElseThrow(() -> new CarTranslationDoesNotExistException(translationId.toString()));

        // Apply locale-specific conversions if needed
        if (dto.getLocale() != null) {
            applyLocaleConversions(dto);

            // Update currency code if not explicitly provided
            if (dto.getCurrencyCode() == null) {
                dto.setCurrencyCode(conversionService.getCurrencyCode(dto.getLocale()));
            }

            // Update miles flag if not explicitly provided
            if (!dto.isMiles()) {
                dto.setMiles(conversionService.isMiles(dto.getLocale()));
            }
        }

        if (dto.getColor() != null) {
            existingTranslation.setColor(dto.getColor());
        }
        if (dto.getDescription() != null) {
            existingTranslation.setDescription(dto.getDescription());
        }
        if (dto.getMileage() != null) {
            existingTranslation.setMileage(dto.getMileage());
        }
        if (dto.getPrice() != null) {
            existingTranslation.setPrice(dto.getPrice());
        }
        if (dto.getCurrencyCode() != null) {
            existingTranslation.setCurrencyCode(dto.getCurrencyCode());
        }
        if (dto.isMiles()) {
            existingTranslation.setMiles(dto.isMiles());
        }

        carTranslationRepository.save(existingTranslation);
    }

    @Override
    @Transactional
    public void deleteCarTranslation(UUID translationId) {
        CarTranslation existingTranslation = carTranslationRepository.findById(translationId)
                .orElseThrow(() -> new CarTranslationDoesNotExistException(translationId.toString()));

        carTranslationRepository.delete(existingTranslation);
    }

    @Override
    public List<CarTranslationDto> getCarTranslations(UUID carId) {
        Car car = carRepository.findById(carId).orElseThrow(() ->
                new CarDoesNotExistException(carId.toString()));
        List<CarTranslation> carTranslations = carTranslationRepository.findAllByCar(car);
        return carTranslations.stream().map(CarTranslationDto::new).toList();
    }

    /**
     * Helper method to apply locale-specific conversions to a DTO
     * This converts mileage and price based on the specified locale
     */
    private void applyLocaleConversions(CarTranslationDto dto) {
        if (dto.getLocale() != null) {
            // Convert mileage if needed
            if (dto.getMileage() != null) {
                dto.setMileage(conversionService.convertMileage(dto.getMileage(), dto.getLocale()));
            }

            // Convert price if needed
            if (dto.getPrice() != null) {
                dto.setPrice(conversionService.convertPrice(dto.getPrice(), dto.getLocale()));
            }
        }
    }
}