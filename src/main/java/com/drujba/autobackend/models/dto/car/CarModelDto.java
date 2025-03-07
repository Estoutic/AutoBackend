package com.drujba.autobackend.models.dto.car;

import com.drujba.autobackend.db.entities.car.CarModel;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CarModelDto {

    private String brand;

    private String model;

    private String generation;

    public CarModelDto(CarModel carModel) {
        this.brand = carModel.getBrand();
        this.model = carModel.getModel();
        this.generation = carModel.getGeneration();
    }
}
