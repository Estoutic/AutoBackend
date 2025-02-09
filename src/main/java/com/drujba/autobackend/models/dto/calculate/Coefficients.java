package com.drujba.autobackend.models.dto.calculate;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class Coefficients {

    private BigDecimal customsDutyCoefficient;
    // Коэффициент для расчёта пошлины (excise fee) – может быть задан как %
    private BigDecimal exciseCoefficient;
    // Коэффициент для расчёта утилизационного сбора
    private BigDecimal utilizationCoefficient;
}
