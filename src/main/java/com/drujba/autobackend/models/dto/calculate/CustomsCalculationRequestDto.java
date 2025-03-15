package com.drujba.autobackend.models.dto.calculate;

import com.drujba.autobackend.models.enums.car.EngineType;
import com.drujba.autobackend.models.enums.calculate.CarAgeCategory;
import com.drujba.autobackend.models.enums.calculate.VehicleOwnerType;
import lombok.Data;

@Data
public class CustomsCalculationRequestDto {
    private CarAgeCategory age;       // Возраст авто
    private int engineCapacity;       // Объём двигателя
    private EngineType engineType;    // Тип двигателя
    private int power;                // Мощность (л.с.)
    private double price;             // Стоимость авто
    private VehicleOwnerType ownerType;  // Тип владельца
    private String currency;          // Валюта
    private String mode;              // "ETC" или "CTP" (или любой другой, если нужно)
}