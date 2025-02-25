package com.drujba.autobackend.models.dto.car;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.db.entities.translation.CarTranslation;
import com.drujba.autobackend.models.enums.Locale;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CarResponseDto {

    private UUID id;

    private UUID carModelId;

    private String name;

    private Integer year;

    private String description;

    private String color;

    private BigDecimal mileage;

    private Integer ownersCount;

    private String transmissionType;

    private String bodyType;

    private String enginePower;

    private String engineType;

    private String driveType;

    private String engineCapacity;

    private String steeringPosition;

    private Integer seatsCount;

    private BigDecimal price;

    private List<String> images;

    private Boolean isAvailable;

    private Timestamp createdAt;

    public CarResponseDto(Car car, CarTranslation carTranslation, List<String> images) {

        Locale locale = carTranslation.getLocale();
        this.id = car.getId();
        this.name = String.format("%s %s %s", car.getCarModel().getBrand(), car.getCarModel().getModel(), car.getCarModel().getGeneration());
        this.carModelId = car.getCarModel().getId();
        this.color = carTranslation.getColor();
        this.description = carTranslation.getDescription();
        this.mileage = carTranslation.getMileage();
        this.price = carTranslation.getPrice();

        this.images = images;
        this.year = car.getYear();
        this.ownersCount = car.getOwnersCount();
        this.transmissionType = car.getTransmissionType().getLocalizedValue(locale);
        this.bodyType = car.getBodyType().getLocalizedValue(locale);
        this.enginePower = String.valueOf(car.getEnginePower());
        this.engineType = car.getEngineType().getLocalizedValue(locale);
        this.driveType = car.getDriveType().getLocalizedValue(locale);
        this.engineCapacity = String.valueOf(car.getEngineCapacity());
        this.steeringPosition = car.getSteeringPosition().getLocalizedValue(locale);
        this.seatsCount = car.getSeatsCount();

        this.isAvailable = car.isAvailable();
        this.createdAt = Timestamp.from(car.getCreatedAt());
    }

    public CarResponseDto(Car car, List<String> images) {
        this.id = car.getId();
        this.carModelId = car.getCarModel().getId();
        this.name = String.format("%s %s %s", car.getCarModel().getBrand(), car.getCarModel().getModel(), car.getCarModel().getGeneration());

        this.color = car.getColor();
        this.description = car.getDescription();
        this.mileage = car.getMileage();
        this.price = car.getPrice();

        this.images = images;
        this.year = car.getYear();
        this.ownersCount = car.getOwnersCount();
        this.transmissionType = car.getTransmissionType().getLocalizedValue(Locale.EU);
        this.bodyType = car.getBodyType().getLocalizedValue(Locale.EU);
        this.enginePower = String.valueOf(car.getEnginePower());
        this.engineType = car.getEngineType().getLocalizedValue(Locale.EU);
        this.driveType = car.getDriveType().getLocalizedValue(Locale.EU);
        this.engineCapacity = String.valueOf(car.getEngineCapacity());
        this.steeringPosition = car.getSteeringPosition().getLocalizedValue(Locale.EU);
        this.seatsCount = car.getSeatsCount();
        this.isAvailable = car.isAvailable();
        this.createdAt = Timestamp.from(car.getCreatedAt());
    }

    public CarResponseDto(Car car, Locale locale) {
        this.id = car.getId();
        this.carModelId = car.getCarModel().getId();
        this.color = car.getColor();
        this.name = String.format("%s %s %s", car.getCarModel().getBrand(), car.getCarModel().getModel(), car.getCarModel().getGeneration());
//        todo добавить состояние машины
        this.description = car.getDescription();
        this.mileage = car.getMileage();
        this.price = car.getPrice();

        this.year = car.getYear();
        this.ownersCount = car.getOwnersCount();
        this.transmissionType = car.getTransmissionType().getLocalizedValue(locale);
        this.bodyType = car.getBodyType().getLocalizedValue(locale);
        this.enginePower = String.valueOf(car.getEnginePower());
        this.engineType = car.getEngineType().getLocalizedValue(locale);
        this.driveType = car.getDriveType().getLocalizedValue(locale);
        this.engineCapacity = String.valueOf(car.getEngineCapacity());
        this.steeringPosition = car.getSteeringPosition().getLocalizedValue(locale);
        this.seatsCount = car.getSeatsCount();
        this.isAvailable = car.isAvailable();
        this.createdAt = Timestamp.from(car.getCreatedAt());
    }

    public CarResponseDto(Car car) {
        this.id = car.getId();
        this.carModelId = car.getCarModel().getId();
        this.color = car.getColor();
        this.name = String.format("%s %s %s", car.getCarModel().getBrand(), car.getCarModel().getModel(), car.getCarModel().getGeneration());
        this.description = car.getDescription();
        this.mileage = car.getMileage();
        this.price = car.getPrice();

        this.year = car.getYear();
        this.ownersCount = car.getOwnersCount();
        this.transmissionType = car.getTransmissionType().getLocalizedValue(Locale.EU);
        this.bodyType = car.getBodyType().getLocalizedValue(Locale.EU);
        this.enginePower = String.valueOf(car.getEnginePower());
        this.engineType = car.getEngineType().getLocalizedValue(Locale.EU);
        this.driveType = car.getDriveType().getLocalizedValue(Locale.EU);
        this.engineCapacity = String.valueOf(car.getEngineCapacity());
        this.steeringPosition = car.getSteeringPosition().getLocalizedValue(Locale.EU);
        this.seatsCount = car.getSeatsCount();
        this.isAvailable = car.isAvailable();
        this.createdAt = Timestamp.from(car.getCreatedAt());
    }
}
