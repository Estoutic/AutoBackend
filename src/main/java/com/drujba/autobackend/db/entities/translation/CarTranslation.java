package com.drujba.autobackend.db.entities.translation;


import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.models.dto.translation.CarTranslationDto;
import com.drujba.autobackend.models.enums.Locale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "car_translations")
public class CarTranslation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "car_id", nullable = false)
    private Car car;

    @Enumerated(EnumType.STRING)
    private Locale locale;

    private String color;

    private String description;

    private int mileage;

    private double price;

    public CarTranslation(CarTranslationDto carTranslationDto, Car car) {
        this.car = car;
        this.locale = carTranslationDto.getLocale();
        this.color = carTranslationDto.getColor();
        this.description = carTranslationDto.getDescription();
        this.mileage = carTranslationDto.getMileage();
        this.price = carTranslationDto.getPrice();
    }
}
