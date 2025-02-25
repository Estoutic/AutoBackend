package com.drujba.autobackend.models.dto.calculate;

import com.drujba.autobackend.models.enums.car.EngineType;
import com.drujba.autobackend.models.enums.calculate.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class CustomsCalculationRequestDto {
    @NotNull
    private Country destinationCountry; // RUSSIA или BELARUS

    private Boolean importFromEaec;

    @NotNull
    private CarAgeCategory carAgeCategory; // NEW, LESS_THAN_3, BETWEEN_3_AND_5, BETWEEN_5_AND_7, MORE_THAN_7

    @NotNull
    @Min(1)
    private Integer engineVolume; // объем двигателя в куб. см

    @NotNull
    private BigDecimal enginePower; // мощность двигателя

    @NotNull
    private EnginePowerUnit enginePowerUnit; // HP или KW

    @NotNull
    private EngineType engineType; // NON_HYBRID, ELECTRO_HYBRID, PHEV

    private HybridPowerComparison hybridPowerComparison; // DVSGreaterThanED или DVSLessThanED
    private Boolean sequentialPowerUnitPresent; // наличие силовой установки последовательного типа

    @NotNull
    private BigDecimal carPrice;
    @NotNull
    private Currency currency; // CNY, USD, EUR, RUB

    @NotNull
    private BuyerType buyerType; // PRIVATE_PERSON, PRIVATE_USE, LEGAL_ENTITY
}
