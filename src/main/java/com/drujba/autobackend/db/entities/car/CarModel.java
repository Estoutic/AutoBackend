package com.drujba.autobackend.db.entities.car;

import com.drujba.autobackend.models.dto.auto.CarModelDto;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "car_models")
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