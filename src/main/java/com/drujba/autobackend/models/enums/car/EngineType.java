package com.drujba.autobackend.models.enums.car;

import com.drujba.autobackend.models.enums.LocalizableEnum;

public enum EngineType implements LocalizableEnum {
    GASOLINE("gasoline"),
    DIESEL("diesel"),
    ELECTRIC("electric"),
    HYBRID("hybrid");

    private final String value;

    EngineType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
