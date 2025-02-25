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
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CarFilterDto {

    private Integer mileageFrom;
    private Integer mileageTo;
    private String city;
    private String brand;
    private String model;
    private String generation;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private Integer ownersCountFrom;
    private Integer ownersCountTo;
    private TransmissionType transmission;
    private BodyType bodyType;
    private Integer yearFrom;
    private Integer yearTo;
    private Integer enginePowerFrom;
    private Integer enginePowerTo;
    private EngineType engineType;
    private DriveType drive;
    private BigDecimal engineCapacityFrom;
    private BigDecimal engineCapacityTo;
    private SteeringPosition steeringPosition;
    private Integer seatsFrom;
    private Integer seatsTo;
    private String sortBy;
    private String sortOrder;
}
