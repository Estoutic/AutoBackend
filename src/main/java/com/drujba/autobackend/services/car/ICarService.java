package com.drujba.autobackend.services.car;

import com.drujba.autobackend.models.dto.car.CarCreationDto;
import com.drujba.autobackend.models.dto.car.CarDto;
import com.drujba.autobackend.models.dto.car.CarFilterDto;
import com.drujba.autobackend.models.dto.car.CarUpdateDto;
import com.drujba.autobackend.models.enums.Locale;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface ICarService {

    UUID saveCar(CarCreationDto carCreationDto);

    void deleteCar(UUID id);

    void updateCar(UUID id, CarUpdateDto carUpdateDto);

    CarDto getCar(UUID id, Locale locale);

    List<CarDto> getAllCars();

    void hideCar(UUID id);

    Page<CarDto> getFilteredCars(CarFilterDto filterDto, Pageable pageable, Locale locale);
}
