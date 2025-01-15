package com.drujba.autobackend.services.car.impl;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.car.CarModel;
import com.drujba.autobackend.db.repostiories.car.CarModelRepository;
import com.drujba.autobackend.db.repostiories.car.CarRepository;
import com.drujba.autobackend.exceptions.car.CarDoesNotExistException;
import com.drujba.autobackend.exceptions.car.CarModelDoesNotExistException;
import com.drujba.autobackend.models.dto.car.CarCreationDto;
import com.drujba.autobackend.models.dto.car.CarDto;
import com.drujba.autobackend.models.dto.car.CarUpdateDto;
import com.drujba.autobackend.services.car.ICarService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CarService implements ICarService {

    private final CarRepository carRepository;
    private final CarModelRepository carModelRepository;

    @Override
    public UUID saveCar(CarCreationDto carCreationDto) {
        CarModel carModel = carModelRepository.findByGeneration(carCreationDto.getGeneration())
                .orElseThrow(() -> new CarModelDoesNotExistException(carCreationDto.getGeneration()));
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

}
