package com.drujba.autobackend.models.dto.car;


import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.models.enums.auto.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarDto {

    private UUID id;

    private UUID carModelId;

    private Integer year;

    private String description;

    private String color;

    private BigDecimal mileage;

    private Integer ownersCount;

    private TransmissionType transmissionType;

    private BodyType bodyType;

    private String enginePower;

    private EngineType engineType;

    private DriveType driveType;

    private String engineCapacity;

    private SteeringPosition steeringPosition;

    private Integer seatsCount;

    private BigDecimal price;

    private Boolean isAvailable;

    private Timestamp createdAt;

    public CarDto(Car car) {
        this.id = car.getId();
        this.carModelId = car.getCarModel().getId();
        this.year = car.getYear();
        this.description = car.getDescription();
        this.color = car.getColor();
        this.mileage = BigDecimal.valueOf(car.getMileage());
        this.ownersCount = car.getOwnersCount();
        this.transmissionType = car.getTransmissionType();
        this.bodyType = car.getBodyType();
        this.enginePower = String.valueOf(car.getEnginePower());
        this.engineType = car.getEngineType();
        this.driveType = car.getDriveType();
        this.engineCapacity = String.valueOf(car.getEngineCapacity());
        this.steeringPosition = car.getSteeringPosition();
        this.seatsCount = car.getSeatsCount();
        this.price = BigDecimal.valueOf(car.getPrice());
        this.isAvailable = car.isAvailable();
        this.createdAt = Timestamp.from(car.getCreatedAt());
    }
}
