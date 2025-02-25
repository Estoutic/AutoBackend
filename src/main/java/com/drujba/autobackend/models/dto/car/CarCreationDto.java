package com.drujba.autobackend.models.dto.car;

import com.drujba.autobackend.models.enums.car.*;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CarCreationDto {

    private CarModelDto carModelDto;

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

    private String vin;
}
