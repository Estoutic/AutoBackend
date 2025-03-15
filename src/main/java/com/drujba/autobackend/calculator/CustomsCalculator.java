package com.drujba.autobackend.calculator;

import com.drujba.autobackend.configs.CustomsConfig;
import com.drujba.autobackend.exceptions.calculator.WrongParamException;
import com.drujba.autobackend.models.dto.calculate.CustomsCalculationResponseDto;
import com.drujba.autobackend.models.enums.calculate.CarAgeCategory;
import com.drujba.autobackend.models.enums.calculate.VehicleOwnerType;
import com.drujba.autobackend.models.enums.car.EngineType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.Map;
import java.util.logging.Level;


@Service
@Slf4j
public class CustomsCalculator {


    // Константы тарифов
    private static final double BASE_VAT = 0.2;
    private static final int RECYCLING_FEE_BASE_RATE = 20000;


    // Параметры транспортного средства
    private CarAgeCategory vehicleAge;
    private int engineCapacity; // см³
    private EngineType engineType;
    private int vehiclePower;   // л.с.
    private double vehiclePrice;
    private VehicleOwnerType ownerType;
    private String vehicleCurrency = "USD";



    private final CustomsConfig config;

    public CustomsCalculator(CustomsConfig config) {
        this.config = config;
    }

    // В реальном проекте можно реализовать запрос к API для получения курса
    public double convertToLocalCurrency(double amount, String currency) {
        if ("RUB".equalsIgnoreCase(currency)) {
            return amount;
        }
        double rate;
        switch (currency.toUpperCase()) {
            case "USD":
                rate = 75.0;
                break;
            case "EUR":
                rate = 90.0;
                break;
            default:
                rate = 1.0;
                break;
        }
        double converted = amount * rate;
        log.info(String.format("Converted %.2f %s to %.2f RUB", amount, currency, converted));
        return converted;
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
        this.vehicleAge = age;
        this.engineCapacity = engineCapacity;
        this.engineType = engineType;
        this.vehiclePower = power;
        this.vehiclePrice = price;
        this.ownerType = ownerType;
        this.vehicleCurrency = currency.toUpperCase();
    }

    /**
     * Расчёт таможенных платежей по методу ETC
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

        // Прочие сборы
        int clearanceFee = config.getBaseClearanceFee();
        int baseUtilFee = config.getBaseUtilFee();
        double recyclingFee = calculateRecyclingFee();
        double utilFee = baseUtilFee; // Для ETC нет доп. коэффициента (в вашем коде etcUtilCoeffBase = 1.0)

        // Акциз (в ETC обычно может не учитываться,
        // но если у вас по логике в ETC тоже идет excise – то раскомментируйте)
        double exciseRub = 0.0;
        // double exciseRub = calculateExcise();

        // НДС (VAT) в ETC, если применяется, можно добавить (у вас в примере нет)
        double vatRub = 0.0;
        // double vatRub = (priceRub + dutyRub + exciseRub) * BASE_VAT;

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

    // Пример метода для расчёта CTP (возвращаем DTO)
    public CustomsCalculationResponseDto calculateCTPAsDto() throws Exception {
        double priceRub = convertToLocalCurrency(vehiclePrice, vehicleCurrency);

        // Минимальный сбор: 0.44 EUR/cm³ => в рублях
        double minDutyPerCc = convertToLocalCurrency(0.44, "EUR");
        double dutyRub = Math.max(priceRub * 0.2, minDutyPerCc * engineCapacity);

        double exciseRub = calculateExcise();
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
        Map<String, Double> adjustments = null;
        if (recyclingFactors.containsKey("adjustments")) {
            Object adjustmentsRaw = recyclingFactors.get("adjustments");

            if (adjustmentsRaw != null) {
                Map<String, Map<String, Double>> allAdjustments = (Map<String, Map<String, Double>>) adjustmentsRaw;
                if (allAdjustments.containsKey(vehicleAge.getValue())) {
                    adjustments = allAdjustments.get(vehicleAge.getValue());
                }
            } else {
                throw new IllegalStateException("Adjustments are not structured correctly in configuration.");
            }
        }

        double engineDefault = defaultFactors.getOrDefault(this.engineType.getValue(), 1.0);
        double engineFactor = engineDefault;

        if (adjustments != null && adjustments.containsKey(this.engineType.getValue())) {
            engineFactor = adjustments.get(this.engineType.getValue());
        }

        double fee = config.getBaseUtilFee() * engineFactor;
        log.info("Recycling fee: " + fee + " RUB");
        return fee;
    }

    /**
     * Расчёт акциза по мощности двигателя
     */
    private double calculateExcise() throws Exception {
        Map<String, Integer> exciseRates = config.getExciseRates();
        if (!exciseRates.containsKey(this.engineType.getValue())) {
            throw new Exception("Missing excise rate for engine type: " + this.engineType.getValue());
        }
        double exciseRate = ((Number) exciseRates.get(this.engineType.getValue())).doubleValue();
        double excise = this.vehiclePower * exciseRate;
        log.info("Excise: " + excise + " RUB");
        return excise;
    }

    /**
     * Вывод результатов расчёта в виде таблицы
     */
//    public void printTable(String mode) throws Exception {
//        Map<String, Double> results;
//        if ("ETC".equalsIgnoreCase(mode)) {
//            results = calculateETC();
//            System.out.println("=== Результаты расчёта по ETC ===");
//        } else if ("CTP".equalsIgnoreCase(mode)) {
//            results = calculateCTP();
//            System.out.println("=== Результаты расчёта по CTP ===");
//        } else {
//            throw new WrongParamException("Invalid calculation mode: " + mode);
//        }
//
//        NumberFormat nf = NumberFormat.getInstance(Locale.US);
//        nf.setMinimumFractionDigits(2);
//        nf.setMaximumFractionDigits(2);
//        System.out.println("+--------------------------+-------------------+");
//        System.out.println("| Description              | Amount (RUB)      |");
//        System.out.println("+--------------------------+-------------------+");
//        for (Map.Entry<String, Double> entry : results.entrySet()) {
//            String key = entry.getKey();
//            Double value = entry.getValue();
//            // Если поле Mode используется только как индикатор режима, можно его пропустить в выводе
//            if ("Mode".equals(key)) {
//                continue;
//            }
//            System.out.printf("| %-24s | %17s |\n", key, nf.format(value));
//        }
//        System.out.println("+--------------------------+-------------------+");
//    }
}