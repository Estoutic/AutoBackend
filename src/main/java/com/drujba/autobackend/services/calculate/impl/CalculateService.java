package com.drujba.autobackend.services.calculate.impl;

import com.drujba.autobackend.models.dto.calculate.Coefficients;
import com.drujba.autobackend.models.dto.calculate.CustomsCalculationRequestDto;
import com.drujba.autobackend.models.dto.calculate.CustomsCalculationResponseDto;
import com.drujba.autobackend.models.enums.auto.EngineType;
import com.drujba.autobackend.models.enums.calculate.*;
import com.drujba.autobackend.services.calculate.ICalculateService;
import lombok.RequiredArgsConstructor;
import org.checkerframework.checker.units.qual.C;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

@Service
@RequiredArgsConstructor
public class CalculateService implements ICalculateService {

//    private CurrencyService currencyService;

    @Override
    public CustomsCalculationResponseDto calculate(CustomsCalculationRequestDto request) {
        // 1. Пересчет цены автомобиля в рубли
//        BigDecimal exchangeRate = currencyService.getExchangeRate(request.getCurrency());
        BigDecimal exchangeRate = BigDecimal.valueOf(92.071);
        BigDecimal carPriceInRub = request.getCarPrice().multiply(exchangeRate);

        // 2. Определение коэффициентов (ставок) по параметрам автомобиля
        Coefficients coeffs = determineCoefficients(request);

        // 3. Расчёт таможенного сбора
        BigDecimal customsDuty = calculateCustomsDuty(carPriceInRub, coeffs);

        // 4. Расчёт пошлины (excise fee)
        BigDecimal exciseFee = calculateExciseFee(carPriceInRub, coeffs);

        // 5. Расчёт утилизационного сбора
        BigDecimal utilizationFee = calculateUtilizationFee(request, coeffs);

        // 6. Для юридических лиц – дополнительно НДС и акциз
        BigDecimal vatAndExcise = BigDecimal.ZERO;
        if (request.getBuyerType() == BuyerType.LEGAL_ENTITY) {
            vatAndExcise = calculateVatAndExciseForLegalEntities(carPriceInRub);
        }

        // 7. Общая сумма сборов
        BigDecimal total = customsDuty.add(exciseFee).add(utilizationFee).add(vatAndExcise);

        // Формирование ответа
        CustomsCalculationResponseDto response = new CustomsCalculationResponseDto();
        response.setCarPriceInRubles(carPriceInRub.setScale(2, RoundingMode.HALF_UP));
        response.setCustomsDuty(customsDuty);
        response.setExciseFee(exciseFee);
        response.setUtilizationFee(utilizationFee);
        response.setVatAndExciseForLegalEntities(vatAndExcise);
        response.setTotalAmount(total);
        return response;
    }

    /**
     * Определяет коэффициенты на основе объема двигателя, возраста и дополнительных параметров.
     */
    private Coefficients determineCoefficients(CustomsCalculationRequestDto request) {
        Coefficients coeffs = new Coefficients();

        // Пример: выбор коэффициента по объему двигателя (таблица упрощенная)
        int volume = request.getEngineVolume();
        if (volume <= 1000) {
            coeffs.setCustomsDutyCoefficient(new BigDecimal("1.65"));
            coeffs.setExciseCoefficient(new BigDecimal("0.17"));
            coeffs.setUtilizationCoefficient(new BigDecimal("1.0"));
        } else if (volume <= 2000) {
            coeffs.setCustomsDutyCoefficient(new BigDecimal("4.2"));
            coeffs.setExciseCoefficient(new BigDecimal("0.26"));
            coeffs.setUtilizationCoefficient(new BigDecimal("1.5"));
        } else if (volume <= 3000) {
            coeffs.setCustomsDutyCoefficient(new BigDecimal("6.3"));
            coeffs.setExciseCoefficient(new BigDecimal("0.17"));
            coeffs.setUtilizationCoefficient(new BigDecimal("2.0"));
        } else {
            coeffs.setCustomsDutyCoefficient(new BigDecimal("9.08"));
            coeffs.setExciseCoefficient(new BigDecimal("0.17"));
            coeffs.setUtilizationCoefficient(new BigDecimal("2.5"));
        }

        // Корректировка коэффициента по возрасту автомобиля
        switch (request.getCarAgeCategory()) {
            case NEW_UNDER_3_YEARS:
                // для новых авто – базовый коэффициент
//                break;
//            case LESS_THAN_3:
                coeffs.setCustomsDutyCoefficient(coeffs.getCustomsDutyCoefficient().multiply(new BigDecimal("1.1"), MathContext.DECIMAL128));
                break;
            case FROM_3_TO_5_YEARS:
                coeffs.setCustomsDutyCoefficient(coeffs.getCustomsDutyCoefficient().multiply(new BigDecimal("1.2"), MathContext.DECIMAL128));
                break;
            case FROM_5_TO_7_YEARS:
                coeffs.setCustomsDutyCoefficient(coeffs.getCustomsDutyCoefficient().multiply(new BigDecimal("1.3"), MathContext.DECIMAL128));
                break;
            case OVER_7_YEARS:
                coeffs.setCustomsDutyCoefficient(coeffs.getCustomsDutyCoefficient().multiply(new BigDecimal("1.4"), MathContext.DECIMAL128));
                break;
        }

        // Корректировка для гибридных автомобилей (пример логики)
        if (request.getEngineType() == EngineType.HYBRID) {
            if (request.getHybridPowerComparison() != null) {
                if (request.getHybridPowerComparison() == HybridPowerComparison.DVSGreaterThanED) {
                    coeffs.setCustomsDutyCoefficient(coeffs.getCustomsDutyCoefficient().multiply(new BigDecimal("0.95"), MathContext.DECIMAL128));
                } else if (request.getHybridPowerComparison() == HybridPowerComparison.DVSLessThanED) {
                    coeffs.setCustomsDutyCoefficient(coeffs.getCustomsDutyCoefficient().multiply(new BigDecimal("1.05"), MathContext.DECIMAL128));
                }
            }
            if (Boolean.TRUE.equals(request.getSequentialPowerUnitPresent())) {
                coeffs.setCustomsDutyCoefficient(coeffs.getCustomsDutyCoefficient().multiply(new BigDecimal("0.9"), MathContext.DECIMAL128));
            }
        }

        // Если страна – Беларусь, а также если предусмотрены льготы (например, импорт для определённых категорий),
        // применим скидку. Здесь для демонстрации уменьшение на 50%.
        if (request.getDestinationCountry() == Country.BELARUS) {
            coeffs.setCustomsDutyCoefficient(coeffs.getCustomsDutyCoefficient().multiply(new BigDecimal("0.5"), MathContext.DECIMAL128));
        }

        return coeffs;
    }

    /**
     * Расчет таможенного сбора. Пример: сбор рассчитывается как процент от таможенной стоимости.
     * Формула: customsDuty = carPriceInRub * (customsDutyCoefficient / 100)
     */
    private BigDecimal calculateCustomsDuty(BigDecimal priceInRub, Coefficients coeffs) {
        BigDecimal dutyRate = coeffs.getCustomsDutyCoefficient().divide(new BigDecimal("100"), MathContext.DECIMAL128);
        return priceInRub.multiply(dutyRate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Расчет пошлины (excise fee). Пример: пошлина = carPriceInRub * (exciseCoefficient / 100)
     */
    private BigDecimal calculateExciseFee(BigDecimal priceInRub, Coefficients coeffs) {
        BigDecimal exciseRate = coeffs.getExciseCoefficient().divide(new BigDecimal("100"), MathContext.DECIMAL128);
        return priceInRub.multiply(exciseRate).setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Расчет утилизационного сбора.
     * Пример: utilizationFee = базовая ставка * utilizationCoefficient.
     * Дополнительно учитываем, если автомобиль ввозится с территории ЕАЭС (для Беларуси).
     */
    private BigDecimal calculateUtilizationFee(CustomsCalculationRequestDto request, Coefficients coeffs) {
        // Базовая ставка для утилизационного сбора (может отличаться для разных категорий ТС)
        BigDecimal baseRate = new BigDecimal("20000"); // например, 20 000 руб.
        BigDecimal fee = baseRate.multiply(coeffs.getUtilizationCoefficient());
        // Если для Беларуси и выбран импорт с ЕАЭС – добавляем дополнительную сумму
        if (request.getDestinationCountry() == Country.BELARUS && Boolean.TRUE.equals(request.getImportFromEaec())) {
            fee = fee.add(new BigDecimal("5000")); // пример: добавить 5000 руб.
        }
        return fee.setScale(2, RoundingMode.HALF_UP);
    }

    /**
     * Дополнительные платежи для юридических лиц: НДС и акциз.
     * Пример: НДС 20% и акциз 10% от таможенной стоимости.
     */
    private BigDecimal calculateVatAndExciseForLegalEntities(BigDecimal priceInRub) {
        BigDecimal vat = priceInRub.multiply(new BigDecimal("0.20"));
        BigDecimal legalExcise = priceInRub.multiply(new BigDecimal("0.10"));
        return vat.add(legalExcise).setScale(2, RoundingMode.HALF_UP);
    }
}


