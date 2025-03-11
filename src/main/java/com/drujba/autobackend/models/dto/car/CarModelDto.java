package com.drujba.autobackend.models.dto.car;

import com.drujba.autobackend.db.entities.car.CarModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CarModelDto {

    private UUID carModelId;

    private String brand;

    private String model;

    private String generation;

    public CarModelDto(CarModel carModel) {
        this.carModelId = carModel.getId();
        this.brand = carModel.getBrand();
        this.model = carModel.getModel();
        this.generation = carModel.getGeneration();
    }
}
