package com.drujba.autobackend.models.enums.calculate;

import java.util.Map;

public enum UtilizationFeeCoefficient {

    ELECTRIC_NEW(1.63), ELECTRIC_OLD(6.1),
    ENGINE_0_1000_NEW(1.65), ENGINE_0_1000_OLD(6.15),
    ENGINE_1000_2000_NEW(4.2), ENGINE_1000_2000_OLD(15.69),
    ENGINE_2000_3000_NEW(6.3), ENGINE_2000_3000_OLD(24.01),
    ENGINE_3000_3500_NEW(5.73), ENGINE_3000_3500_OLD(28.5),
    ENGINE_3500_PLUS_NEW(9.08), ENGINE_3500_PLUS_OLD(35.01),
    PERSONAL_USE(0.17); // Физлица для личного пользования

    private final double coefficient;

    UtilizationFeeCoefficient(double coefficient) {
        this.coefficient = coefficient;
    }

    public double getCoefficient() {
        return coefficient;
    }

    // Метод для получения коэффициента по параметрам авто
    public static double getCoefficient(boolean isElectric, int engineVolume, boolean isOld, boolean isPersonalUse) {
        if (isPersonalUse) return PERSONAL_USE.coefficient;
        if (isElectric) return isOld ? ELECTRIC_OLD.coefficient : ELECTRIC_NEW.coefficient;
        if (engineVolume <= 1000) return isOld ? ENGINE_0_1000_OLD.coefficient : ENGINE_0_1000_NEW.coefficient;
        if (engineVolume <= 2000) return isOld ? ENGINE_1000_2000_OLD.coefficient : ENGINE_1000_2000_NEW.coefficient;
        if (engineVolume <= 3000) return isOld ? ENGINE_2000_3000_OLD.coefficient : ENGINE_2000_3000_NEW.coefficient;
        if (engineVolume <= 3500) return isOld ? ENGINE_3000_3500_OLD.coefficient : ENGINE_3000_3500_NEW.coefficient;
        return isOld ? ENGINE_3500_PLUS_OLD.coefficient : ENGINE_3500_PLUS_NEW.coefficient;
    }
}