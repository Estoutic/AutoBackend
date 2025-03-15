package com.drujba.autobackend.models.enums.calculate;

import com.drujba.autobackend.models.enums.LocalizableEnum;

enum EnginePowerUnit {
    KW("kilowatt"),
    HP("horsepower");

    private final String unit;

    EnginePowerUnit(String unit) {
        this.unit = unit;
    }

    public String getUnit() {
        return unit;
    }
}