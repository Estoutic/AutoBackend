package com.drujba.autobackend.models.dto.car;

import com.drujba.autobackend.models.enums.auto.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CarUpdateDto {

    private Integer year;

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
}
