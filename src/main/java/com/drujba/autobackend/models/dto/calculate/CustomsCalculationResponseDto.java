package com.drujba.autobackend.models.dto.calculate;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CustomsCalculationResponseDto {
    private String mode;            // Режим расчёта (ETC, CTP и т.д.)
    private double priceRub;        // Стоимость автомобиля в рублях (если актуально)
    private double dutyRub;         // Пошлина
    private double exciseRub;       // Акциз
    private double vatRub;          // НДС
    private double clearanceFee;    // Таможенный сбор
    private double recyclingFee;    // Утилизационный сбор
    private double utilFee;         // Сбор за утилизацию (базовый), если отличается
    private double totalPay;        // Итоговая сумма
}