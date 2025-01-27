package com.drujba.autobackend.models.dto.calculate;

import com.drujba.autobackend.models.enums.auto.EngineType;
import com.drujba.autobackend.models.enums.auto.HybridType;
import com.drujba.autobackend.models.enums.calculate.Age;
import com.drujba.autobackend.models.enums.calculate.CalculationType;
import com.drujba.autobackend.models.enums.calculate.CurrencyCode;
import com.drujba.autobackend.models.enums.calculate.PowerDvsVsEd;

import java.math.BigDecimal;

public class CustomsCalculationRequestDTO {

    private String country;
    private Age age;
    private BigDecimal price;
    private CurrencyCode currency;
    private Integer engineVolume;
    private Integer enginePower;
    private String powerUnit;
    private EngineType engineType;
    private HybridType hybridType;
    private PowerDvsVsEd powerDvsVsEd;
    private CalculationType calculationType;
}
