package com.drujba.autobackend.services.car.impl;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.car.CarModel;
import com.drujba.autobackend.db.entities.car.Image;
import com.drujba.autobackend.db.entities.translation.CarTranslation;
import com.drujba.autobackend.db.repositories.car.CarModelRepository;
import com.drujba.autobackend.db.repositories.car.CarRepository;
import com.drujba.autobackend.db.repositories.car.ImageRepository;
import com.drujba.autobackend.db.repositories.translation.CarTranslationRepository;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.exceptions.car.CarModelDoesNotExistException;
import com.drujba.autobackend.models.dto.car.*;
import com.drujba.autobackend.models.enums.Locale;
import com.drujba.autobackend.services.car.ICarService;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService implements ICarService {

    private final CarRepository carRepository;
    private final CarModelRepository carModelRepository;
    private final CarTranslationRepository carTranslationRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    @CacheEvict(value = "cars", allEntries = true)
    public UUID saveCar(CarCreationDto carCreationDto) {
        CarModelDto carModelDto = carCreationDto.getCarModelDto();
        CarModel carModel = carModelRepository.findByBrandAndModelAndGeneration(
                        carModelDto.getBrand(),
                        carModelDto.getModel(),
                        carModelDto.getGeneration())
                .orElseThrow(() -> new CarModelDoesNotExistException(carCreationDto.getCarModelDto().getModel()));
        Car car = new Car(carCreationDto, carModel);
        return carRepository.save(car).getId();
    }

    @Override
    @Transactional
    @CacheEvict(value = "cars", allEntries = true)
    public void deleteCar(UUID id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
        }
    }

    @Override
    @Transactional
    @CacheEvict(value = "cars", allEntries = true)
    public void updateCar(UUID id, CarUpdateDto carUpdateDto) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarDoesNotExistException(id.toString()));

        if (carUpdateDto.getYear() != null) {
            car.setYear(carUpdateDto.getYear());
        }
        if (carUpdateDto.getColor() != null) {
            car.setColor(carUpdateDto.getColor());
        }
        if (carUpdateDto.getMileage() != null) {
            car.setMileage(carUpdateDto.getMileage());
        }
        if (carUpdateDto.getOwnersCount() != null) {
            car.setOwnersCount(carUpdateDto.getOwnersCount());
        }
        if (carUpdateDto.getTransmissionType() != null) {
            car.setTransmissionType(carUpdateDto.getTransmissionType());
        }
        if (carUpdateDto.getBodyType() != null) {
            car.setBodyType(carUpdateDto.getBodyType());
        }
        if (carUpdateDto.getEnginePower() != null) {
            car.setEnginePower(Integer.parseInt(carUpdateDto.getEnginePower()));
        }
        if (carUpdateDto.getEngineType() != null) {
            car.setEngineType(carUpdateDto.getEngineType());
        }
        if (carUpdateDto.getDriveType() != null) {
            car.setDriveType(carUpdateDto.getDriveType());
        }
        if (carUpdateDto.getEngineCapacity() != null) {
            car.setEngineCapacity(Double.parseDouble(carUpdateDto.getEngineCapacity()));
        }
        if (carUpdateDto.getSteeringPosition() != null) {
            car.setSteeringPosition(carUpdateDto.getSteeringPosition());
        }
        if (carUpdateDto.getSeatsCount() != null) {
            car.setSeatsCount(carUpdateDto.getSeatsCount());
        }
        if (carUpdateDto.getPrice() != null) {
            car.setPrice(carUpdateDto.getPrice());
        }

        carRepository.save(car);
    }

    @Override
    @Cacheable(value = "cars", key = "#id + '-' + #locale")
    public CarResponseDto getCar(UUID id, Locale locale) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarDoesNotExistException(id.toString()));

        CarTranslation translation = carTranslationRepository.findByCarAndLocale(car, locale)
                .orElseGet(() -> carTranslationRepository.findByCarAndLocale(car, Locale.EU).orElse(null));

        List<String> images = imageRepository.findByCar(car).stream()
                .map(Image::getFilePath)
                .collect(Collectors.toList());

        if (translation != null) {
            return new CarResponseDto(car, translation, images);
        }
        return new CarResponseDto(car, images);
    }

    @Override
    @Transactional
    @CacheEvict(value = "cars", allEntries = true)
    public void hideCar(UUID id) {
        Car car = carRepository.findById(id)
                .orElseThrow(() -> new CarDoesNotExistException(id.toString()));
        car.setAvailable(false);
        carRepository.save(car);
    }

    @Override
    @Cacheable(value = "cars", key = "{#pageable.pageNumber, #pageable.pageSize, #locale, #filterDto.hashCode()}")
    @Transactional(readOnly = true)
    public Page<CarResponseDto> getFilteredCars(CarFilterDto filterDto, Pageable pageable, Locale locale) {
        Specification<Car> spec = buildCarSpecification(filterDto);

        // Fetch cars
        Page<Car> cars = carRepository.findAll(spec, pageable);

        if (cars.isEmpty()) {
            return Page.empty(pageable);
        }

        // Get all car IDs to fetch translations and images in batch
        List<UUID> carIds = cars.getContent().stream()
                .map(Car::getId)
                .collect(Collectors.toList());

        // Batch fetch translations for this locale and EU (fallback)
        Map<UUID, CarTranslation> translations = new HashMap<>();

        // First try with the requested locale
        List<CarTranslation> primaryTranslations = carTranslationRepository.findByCarIdInAndLocale(carIds, locale);
        primaryTranslations.forEach(translation ->
                translations.put(translation.getCar().getId(), translation)
        );

        // For cars without translation in requested locale, try EU locale
        List<UUID> missingTranslationCarIds = carIds.stream()
                .filter(id -> !translations.containsKey(id))
                .collect(Collectors.toList());

        if (!missingTranslationCarIds.isEmpty()) {
            List<CarTranslation> fallbackTranslations =
                    carTranslationRepository.findByCarIdInAndLocale(missingTranslationCarIds, Locale.EU);
            fallbackTranslations.forEach(translation ->
                    translations.put(translation.getCar().getId(), translation)
            );
        }

        // Batch fetch images
        Map<UUID, List<String>> imagesByCarId = fetchImagesByCarIds(carIds);

        // Map entities to DTOs
        return cars.map(car -> {
            CarTranslation translation = translations.get(car.getId());
            List<String> images = imagesByCarId.getOrDefault(car.getId(), Collections.emptyList());

            if (translation != null) {
                return new CarResponseDto(car, translation, images);
            } else {
                return new CarResponseDto(car, images);
            }
        });
    }

    private Map<UUID, List<String>> fetchImagesByCarIds(List<UUID> carIds) {
        return imageRepository.findByCarIdIn(carIds)
                .stream()
                .collect(Collectors.groupingBy(
                        image -> image.getCar().getId(),
                        Collectors.mapping(Image::getFilePath, Collectors.toList())
                ));
    }

    private Specification<Car> buildCarSpecification(CarFilterDto filterDto) {
        Specification<Car> spec = Specification.where(null);

        // Filter by availability - Default filter to only show available cars
        spec = spec.and((root, query, criteriaBuilder) ->
                criteriaBuilder.equal(root.get("isAvailable"), true));

        // Mileage filters
        if (filterDto.getMileageFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("mileage"), filterDto.getMileageFrom()));
        }
        if (filterDto.getMileageTo() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("mileage"), filterDto.getMileageTo()));
        }

        // Brand filter
        if (filterDto.getBrand() != null && !filterDto.getBrand().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("carModel").get("brand"), filterDto.getBrand()));
        }

        // Model filter
        if (filterDto.getModel() != null && !filterDto.getModel().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("carModel").get("model"), filterDto.getModel()));
        }

        // Generation filter
        if (filterDto.getGeneration() != null && !filterDto.getGeneration().isEmpty()) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("carModel").get("generation"), filterDto.getGeneration()));
        }

        // Price filters
        if (filterDto.getPriceFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filterDto.getPriceFrom()));
        }
        if (filterDto.getPriceTo() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("price"), filterDto.getPriceTo()));
        }

        // Owners count filters
        if (filterDto.getOwnersCountFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("ownersCount"), filterDto.getOwnersCountFrom()));
        }
        if (filterDto.getOwnersCountTo() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("ownersCount"), filterDto.getOwnersCountTo()));
        }

        // Transmission type filter
        if (filterDto.getTransmission() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("transmissionType"), filterDto.getTransmission()));
        }

        // Body type filter
        if (filterDto.getBodyType() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("bodyType"), filterDto.getBodyType()));
        }

        // Year filters
        if (filterDto.getYearFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("year"), filterDto.getYearFrom()));
        }
        if (filterDto.getYearTo() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("year"), filterDto.getYearTo()));
        }

        // Engine power filters
        if (filterDto.getEnginePowerFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("enginePower"), filterDto.getEnginePowerFrom()));
        }
        if (filterDto.getEnginePowerTo() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("enginePower"), filterDto.getEnginePowerTo()));
        }

        // Engine type filter
        if (filterDto.getEngineType() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("engineType"), filterDto.getEngineType()));
        }

        // Drive type filter
        if (filterDto.getDrive() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("driveType"), filterDto.getDrive()));
        }

        // Engine capacity filters
        if (filterDto.getEngineCapacityFrom() != null) {
            BigDecimal value = filterDto.getEngineCapacityFrom();
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("engineCapacity"), value.doubleValue()));
        }
        if (filterDto.getEngineCapacityTo() != null) {
            BigDecimal value = filterDto.getEngineCapacityTo();
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("engineCapacity"), value.doubleValue()));
        }

        // Steering position filter
        if (filterDto.getSteeringPosition() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.equal(root.get("steeringPosition"), filterDto.getSteeringPosition()));
        }

        // Seats filters
        if (filterDto.getSeatsFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.greaterThanOrEqualTo(root.get("seatsCount"), filterDto.getSeatsFrom()));
        }
        if (filterDto.getSeatsTo() != null) {
            spec = spec.and((root, query, criteriaBuilder) ->
                    criteriaBuilder.lessThanOrEqualTo(root.get("seatsCount"), filterDto.getSeatsTo()));
        }

        return spec;
    }
}