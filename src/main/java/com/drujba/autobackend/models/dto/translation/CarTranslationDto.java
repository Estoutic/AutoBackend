package com.drujba.autobackend.models.dto.translation;

import com.drujba.autobackend.db.entities.translation.CarTranslation;
import com.drujba.autobackend.models.enums.Locale;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
public class CarTranslationDto {
    private UUID id;
    private UUID carId;
    private Locale locale;
    private String color;
    private String description;
    private BigDecimal mileage;
    private BigDecimal price;

    // New fields for currency and distance unit
    private String currencyCode;
    private boolean isMiles;

    public CarTranslationDto(CarTranslation carTranslation) {
        this.id = carTranslation.getId();
        this.locale = carTranslation.getLocale();
        this.color = carTranslation.getColor();
        this.description = carTranslation.getDescription();
        this.mileage = carTranslation.getMileage();
        this.price = carTranslation.getPrice();
        this.currencyCode = carTranslation.getCurrencyCode();
        this.isMiles = carTranslation.isMiles();
    }
}