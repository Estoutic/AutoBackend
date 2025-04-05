package com.drujba.autobackend.db.entities.translation;

import com.drujba.autobackend.db.entities.car.Car;
import com.drujba.autobackend.models.dto.translation.CarTranslationDto;
import com.drujba.autobackend.models.enums.Locale;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "car_translations", indexes = {
        @Index(name = "idx_car_translation_car_locale", columnList = "car_id,locale")
})
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

    private BigDecimal mileage;

    private BigDecimal price;

    // New fields for currency and distance unit
    @Column(name = "currency_code")
    private String currencyCode;

    @Column(name = "is_miles")
    private boolean isMiles;

    public CarTranslation(CarTranslationDto carTranslationDto, Car car) {
        this.car = car;
        this.locale = carTranslationDto.getLocale();
        this.color = carTranslationDto.getColor();
        this.description = carTranslationDto.getDescription();
        this.mileage = carTranslationDto.getMileage();
        this.price = carTranslationDto.getPrice();
        this.currencyCode = carTranslationDto.getCurrencyCode();
        this.isMiles = carTranslationDto.isMiles();

        // If currencyCode is not provided, set default based on locale
        if (this.currencyCode == null && this.locale != null) {
            switch (this.locale) {
                case EU:
                    this.currencyCode = "USD";
                    break;
                case ZH:
                    this.currencyCode = "CNY";
                    break;
                case RU:
                default:
                    this.currencyCode = "RUB";
            }
        }

        // Set isMiles based on locale if not explicitly provided
        if (this.locale == Locale.EU && carTranslationDto.getCurrencyCode() == null) {
            this.isMiles = true;
        }
    }
}