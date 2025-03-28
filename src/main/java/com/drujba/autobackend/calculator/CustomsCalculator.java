package com.drujba.autobackend.calculator;

import com.drujba.autobackend.configs.CustomsConfig;
import com.drujba.autobackend.exceptions.calculator.WrongParamException;
import com.drujba.autobackend.models.dto.calculate.CustomsCalculationResponseDto;
import com.drujba.autobackend.models.enums.calculate.CarAgeCategory;
import com.drujba.autobackend.models.enums.calculate.VehicleOwnerType;
import com.drujba.autobackend.models.enums.car.EngineType;
import com.drujba.autobackend.services.calculate.impl.ExchangeRateService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@Slf4j
public class CustomsCalculator {

    private static final double BASE_VAT = 0.2; // 20% VAT for Russia
    private static final double BASE_EXCISE = 0.0; // Base excise rate

    private CarAgeCategory vehicleAge;
    private int engineCapacity; // см³
    private EngineType engineType;
    private int vehiclePower;   // л.с.
    private double vehiclePrice;
    private VehicleOwnerType ownerType;
    private String vehicleCurrency = "USD";

    private final CustomsConfig config;
    private final ExchangeRateService exchangeRateService;

    public CustomsCalculator(CustomsConfig config, ExchangeRateService exchangeRateService) {
        this.config = config;
        this.exchangeRateService = exchangeRateService;
    }

    /**
     * Converts amount from given currency to RUB using current exchange rates
     */
    public double convertToLocalCurrency(double amount, String currency) {
        if ("RUB".equalsIgnoreCase(currency)) {
            return amount;
        }

        // Нормализуем валюту перед получением курса обмена
        String normalizedCurrency = normalizeToISOCode(currency);

        double rate = exchangeRateService.getExchangeRate(normalizedCurrency);
        double converted = amount * rate;

        log.info(String.format("Converted %.2f %s to %.2f RUB (rate: %.4f)",
                amount, currency, converted, rate));

        return converted;
    }

    /**
     * Нормализует пользовательский ввод валюты в стандартный ISO код
     */
    private String normalizeToISOCode(String currency) {
        if (currency == null) return "USD";

        String normalized = currency.toUpperCase().trim();

        switch (normalized) {
            case "EURO":
                return "EUR";
            case "YEN":
                return "CNY";
            case "YUAN":
            case "CNY":
                return "CNY";
            case "DOLLAR":
                return "USD";
            default:
                return normalized;
        }
    }

    public void resetFields() {
        this.vehicleAge = null;
        this.engineCapacity = 0;
        this.engineType = null;
        this.vehiclePower = 0;
        this.vehiclePrice = 0;
        this.ownerType = null;
        this.vehicleCurrency = "USD";
    }

    public void setVehicleDetails(CarAgeCategory age, int engineCapacity, EngineType engineType, int power, double price, VehicleOwnerType ownerType, String currency) throws WrongParamException {
        if (currency == null || currency.trim().isEmpty()) {
            throw new WrongParamException("Currency cannot be empty");
        }

        this.vehicleAge = age;
        this.engineCapacity = engineCapacity;
        this.engineType = engineType;
        this.vehiclePower = power;
        this.vehiclePrice = price;
        this.ownerType = ownerType;
        this.vehicleCurrency = currency.toUpperCase();
    }

    /**
     * Расчёт таможенных платежей по методу ETC (Единый таможенный сбор)
     * ETC typically does not include VAT and often has different excise handling
     */
    public CustomsCalculationResponseDto calculateETCAsDto() throws Exception {
        double priceRub = convertToLocalCurrency(vehiclePrice, vehicleCurrency);

        // Расчёт пошлины
        Map<String, Map<String, Map<String, Object>>> ageGroups = config.getAgeGroups();
        Map<String, Map<String, Object>> overrides = ageGroups.get("overrides");
        Map<String, Object> groupOverrides = overrides.get(vehicleAge.getValue());
        if (groupOverrides == null) {
            throw new Exception("Missing tariff overrides for age: " + vehicleAge.getValue());
        }
        Map<String, Object> engineTariffs = (Map<String, Object>) groupOverrides.get(this.engineType.getValue());
        if (engineTariffs == null) {
            throw new Exception("Missing tariff for engine type: " + this.engineType.getValue());
        }
        double ratePerCc = ((Number) engineTariffs.get("rate-per-cc")).doubleValue();
        double eurToRub = convertToLocalCurrency(1, "EUR");
        double dutyRub = ratePerCc * engineCapacity * eurToRub;

        // Apply minimum duty if specified
        double minDuty = ((Number) engineTariffs.getOrDefault("min-duty", 0)).doubleValue() * eurToRub;
        if (minDuty > 0 && dutyRub < minDuty) {
            dutyRub = minDuty;
        }

        // Прочие сборы
        int clearanceFee = config.getBaseClearanceFee();
        int baseUtilFee = config.getBaseUtilFee();
        double recyclingFee = calculateRecyclingFee();
        double utilFee = baseUtilFee; // Для ETC нет доп. коэффициента

        // For ETC, excise is only calculated for powerful vehicles (>150 hp) for individuals
        // and all vehicles for companies
        double exciseRub = 0.0;
        if (vehiclePower > 150 || VehicleOwnerType.COMPANY.equals(ownerType)) {
            exciseRub = calculateExcise();
        }

        // VAT (НДС) for ETC is not applied for individuals, only for companies
        double vatRub = 0.0;
        if (VehicleOwnerType.COMPANY.equals(ownerType)) {
            vatRub = (priceRub + dutyRub + exciseRub) * BASE_VAT;
        }

        double totalPay = clearanceFee + dutyRub + utilFee + recyclingFee + exciseRub + vatRub;

        // Возвращаем DTO
        return new CustomsCalculationResponseDto(
                "ETC",
                priceRub,
                dutyRub,
                exciseRub,
                vatRub,
                clearanceFee,
                recyclingFee,
                utilFee,
                totalPay
        );
    }

    /**
     * Расчёт таможенных платежей по методу CTP (Обычная таможенная процедура)
     * CTP includes VAT for all vehicle types and has different duty calculation
     */
    public CustomsCalculationResponseDto calculateCTPAsDto() throws Exception {
        double priceRub = convertToLocalCurrency(vehiclePrice, vehicleCurrency);

        // Минимальный сбор: 0.44 EUR/cm³ => в рублях
        double minDutyPerCc = convertToLocalCurrency(0.44, "EUR");

        // For CTP, duty is max of (20% of price) or (0.44 EUR per cm³)
        double dutyRub = Math.max(priceRub * 0.2, minDutyPerCc * engineCapacity);

        // Excise for all vehicles under CTP
        double exciseRub = calculateExcise();

        // VAT is applied to all vehicles under CTP
        double vatRub = (priceRub + dutyRub + exciseRub) * BASE_VAT;

        int clearanceFee = config.getBaseClearanceFee();
        int baseUtilFee = config.getBaseUtilFee();
        double ctpUtilCoeffBase = config.getCtpUtilCoeffBase();
        double utilFee = baseUtilFee * ctpUtilCoeffBase;

        double recyclingFee = calculateRecyclingFee();

        double totalPay = dutyRub + exciseRub + vatRub + clearanceFee + utilFee + recyclingFee;

        return new CustomsCalculationResponseDto(
                "CTP",
                priceRub,
                dutyRub,
                exciseRub,
                vatRub,
                clearanceFee,
                recyclingFee,
                utilFee,
                totalPay
        );
    }

    /**
     * Расчёт утилизационного сбора
     */
    private double calculateRecyclingFee() {
        // Получаем коэффициенты утилизации из конфигурации
        Map<String, Map<String, Double>> recyclingFactors = config.getRecyclingFactors();

        // Получаем базовые коэффициенты
        Map<String, Double> defaultFactors = recyclingFactors.get("default");
        if (defaultFactors == null) {
            throw new IllegalStateException("Default recycling factors are missing in configuration.");
        }

        // Получаем корректировки коэффициентов (если они есть)
        Map<String, Double> adjustments = recyclingFactors.get("adjustments");

        double engineDefault = defaultFactors.getOrDefault(this.engineType.getValue(), 1.0);
        double engineFactor = engineDefault;

        if (adjustments != null) {
            String adjustmentKey = this.vehicleAge.getValue() + "_" + this.engineType.getValue();

            if (adjustments.containsKey(adjustmentKey)) {
                engineFactor = adjustments.get(adjustmentKey);
            } else if (adjustments.containsKey(this.engineType.getValue())) {
                engineFactor = adjustments.get(this.engineType.getValue());
            }
        }

        double fee = config.getBaseUtilFee() * engineFactor;
        log.info("Recycling fee: " + fee + " RUB");
        return fee;
    }
    /**
     * Расчёт акциза по мощности двигателя
     * For electric vehicles, excise is often waived or reduced
     */
    private double calculateExcise() throws Exception {
        Map<String, Integer> exciseRates = config.getExciseRates();
        if (!exciseRates.containsKey(this.engineType.getValue())) {
            throw new Exception("Missing excise rate for engine type: " + this.engineType.getValue());
        }

        if (EngineType.ELECTRIC.equals(this.engineType)) {
            return 0.0;
        }

        double exciseRate = ((Number) exciseRates.get(this.engineType.getValue())).doubleValue();

        if (this.vehiclePower > 300) {
            exciseRate = exciseRate * 1.5;
        } else if (this.vehiclePower > 200) {
            exciseRate = exciseRate * 1.25;
        }

        double excise = this.vehiclePower * exciseRate;
        log.info("Excise: " + excise + " RUB");
        return excise;
    }
}