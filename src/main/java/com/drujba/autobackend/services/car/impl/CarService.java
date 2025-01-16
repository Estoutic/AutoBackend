package com.drujba.autobackend.services.car.impl;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.car.CarModel;
import com.drujba.autobackend.db.repostiories.car.CarModelRepository;
import com.drujba.autobackend.db.repostiories.car.CarRepository;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.exceptions.car.CarModelDoesNotExistException;
import com.drujba.autobackend.models.dto.car.*;
import com.drujba.autobackend.services.car.ICarService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CarService implements ICarService {

    private final CarRepository carRepository;
    private final CarModelRepository carModelRepository;

    @Override
    public UUID saveCar(CarCreationDto carCreationDto) {
        CarModelDto carModelDto = carCreationDto.getCarModelDto();
        CarModel carModel = carModelRepository.findByBrandAndModelAndGeneration(carModelDto.getBrand(),
                        carModelDto.getModel(),carModelDto.getGeneration())
                .orElseThrow(() -> new CarModelDoesNotExistException(carCreationDto.getCarModelDto().getModel()));
        Car car = new Car(carCreationDto, carModel);
        return carRepository.save(car).getId();
    }

    @Override
    public void deleteCar(UUID id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
        }
    }

    @Override
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
            car.setMileage(carUpdateDto.getMileage().intValue());
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
            car.setPrice(carUpdateDto.getPrice().doubleValue());
        }

        carRepository.save(car);
    }

    @Override
    public CarDto getCar(UUID id) {
        Car car = carRepository.findById(id).orElseThrow(() -> new CarDoesNotExistException(id.toString()));
        return new CarDto(car);
    }

    @Override
    public List<CarDto> getAllCars() {
        return carRepository.findAll().stream().map(CarDto::new).collect(Collectors.toList());
    }

    @Override
    public Page<CarDto> getFilteredCars(CarFilterDto filterDto, Pageable pageable) {
        Specification<Car> spec = Specification.where(null);

        // Фильтр по пробегу
        if (filterDto.getMileageFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get("mileage"), filterDto.getMileageFrom()));
        }
        if (filterDto.getMileageTo() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get("mileage"), filterDto.getMileageTo()));
        }

        // Фильтр по городу
        if (filterDto.getCity() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("city"), filterDto.getCity()));
        }

        // Фильтр по бренду
        if (filterDto.getBrand() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("carModel").get("brand"), filterDto.getBrand()));
        }

        // Фильтр по модели
        if (filterDto.getModel() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("carModel").get("model"), filterDto.getModel()));
        }

        // Фильтр по поколению
        if (filterDto.getGeneration() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("carModel").get("generation"), filterDto.getGeneration()));
        }

        // Фильтр по цене
        if (filterDto.getPriceFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get("price"), filterDto.getPriceFrom()));
        }
        if (filterDto.getPriceTo() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get("price"), filterDto.getPriceTo()));
        }

        // Фильтр по количеству владельцев
        if (filterDto.getOwnersCountFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get("ownersCount"), filterDto.getOwnersCountFrom()));
        }
        if (filterDto.getOwnersCountTo() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get("ownersCount"), filterDto.getOwnersCountTo()));
        }

        // Фильтр по типу трансмиссии
        if (filterDto.getTransmission() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("transmissionType"), filterDto.getTransmission()));
        }

        // Фильтр по типу кузова
        if (filterDto.getBodyType() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("bodyType"), filterDto.getBodyType()));
        }

        // Фильтр по году выпуска
        if (filterDto.getYearFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get("year"), filterDto.getYearFrom()));
        }
        if (filterDto.getYearTo() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get("year"), filterDto.getYearTo()));
        }

        // Фильтр по мощности двигателя
        if (filterDto.getEnginePowerFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get("enginePower"), filterDto.getEnginePowerFrom()));
        }
        if (filterDto.getEnginePowerTo() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get("enginePower"), filterDto.getEnginePowerTo()));
        }

        // Фильтр по типу двигателя
        if (filterDto.getEngineType() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("engineType"), filterDto.getEngineType()));
        }

        // Фильтр по приводу
        if (filterDto.getDrive() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("driveType"), filterDto.getDrive()));
        }

        // Фильтр по объему двигателя
        if (filterDto.getEngineCapacityFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get("engineCapacity"), filterDto.getEngineCapacityFrom()));
        }
        if (filterDto.getEngineCapacityTo() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get("engineCapacity"), filterDto.getEngineCapacityTo()));
        }

        // Фильтр по положению руля
        if (filterDto.getSteeringPosition() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.equal(root.get("steeringPosition"), filterDto.getSteeringPosition()));
        }

        // Фильтр по количеству мест
        if (filterDto.getSeatsFrom() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.greaterThanOrEqualTo(root.get("seatsCount"), filterDto.getSeatsFrom()));
        }
        if (filterDto.getSeatsTo() != null) {
            spec = spec.and((root, query, criteriaBuilder)
                    -> criteriaBuilder.lessThanOrEqualTo(root.get("seatsCount"), filterDto.getSeatsTo()));
        }

        // Применение спецификаций и возврат результатов
        Page<Car> cars = carRepository.findAll(spec, pageable);
        return cars.map(CarDto::new);
    }

}
