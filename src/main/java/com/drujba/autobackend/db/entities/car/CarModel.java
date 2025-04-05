package com.drujba.autobackend.db.entities.car;

import com.drujba.autobackend.models.dto.car.CarModelDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "car_models", indexes = {
        @Index(name = "idx_car_model_brand", columnList = "brand"),
        @Index(name = "idx_car_model_brand_model", columnList = "brand,model"),
        @Index(name = "idx_car_model_unique", columnList = "brand,model,generation", unique = true)
})
public class CarModel {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private String brand;

    private String model;

    private String generation;

    public CarModel(CarModelDto carModelDto) {
        this.brand = carModelDto.getBrand();
        this.model = carModelDto.getModel();
        this.generation = carModelDto.getGeneration();
    }
}