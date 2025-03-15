package com.drujba.autobackend.services.calculate.impl;

import com.drujba.autobackend.calculator.CustomsCalculator;
import com.drujba.autobackend.models.dto.calculate.Coefficients;
import com.drujba.autobackend.models.dto.calculate.CustomsCalculationRequestDto;
import com.drujba.autobackend.models.dto.calculate.CustomsCalculationResponseDto;
import com.drujba.autobackend.models.enums.car.EngineType;
import com.drujba.autobackend.models.enums.calculate.*;
import com.drujba.autobackend.services.calculate.ICalculateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.logging.Level;

@Slf4j
@Service
@RequiredArgsConstructor
public class CalculateService implements ICalculateService {

    private final CustomsCalculator calculator;

    // Метод, принимающий DTO и возвращающий DTO
    public CustomsCalculationResponseDto calculateCustoms(CustomsCalculationRequestDto request) {
        try {
            // 1. Сбрасываем поля, если нужно
            calculator.resetFields();

            // 2. Устанавливаем параметры автомобиля
            calculator.setVehicleDetails(
                    request.getAge(),
                    request.getEngineCapacity(),
                    request.getEngineType(),
                    request.getPower(),
                    request.getPrice(),
                    request.getOwnerType(),
                    request.getCurrency()
            );

            // 3. Вызываем нужный метод расчёта (ETC или CTP)
            if ("ETC".equalsIgnoreCase(request.getMode())) {
                return calculator.calculateETCAsDto();
            } else if ("CTP".equalsIgnoreCase(request.getMode())) {
                return calculator.calculateCTPAsDto();
            } else {
                log.error("Unknown mode: {}", request.getMode());
                // вернём ошибку или бросим исключение
                throw new IllegalArgumentException("Invalid calculation mode: " + request.getMode());
            }

        } catch (Exception e) {
            log.error("Ошибка при расчёте: {}", e.getMessage(), e);
            // В реальном проекте — можно бросить кастомное исключение или вернуть пустой DTO
            throw new RuntimeException(e);
        }
    }
}

