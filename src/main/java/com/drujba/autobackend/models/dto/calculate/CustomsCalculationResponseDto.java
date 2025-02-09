package com.drujba.autobackend.models.dto.calculate;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.checker.units.qual.N;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class CustomsCalculationResponseDto {
    private BigDecimal carPriceInRubles; // стоимость в рублях
    private BigDecimal customsDuty;       // таможенный сбор
    private BigDecimal exciseFee;         // пошлина
    private BigDecimal utilizationFee;    // утилизационный сбор
    private BigDecimal vatAndExciseForLegalEntities; // НДС и акциз (для юридических лиц)
    private BigDecimal totalAmount;       // общая сумма сборов
}