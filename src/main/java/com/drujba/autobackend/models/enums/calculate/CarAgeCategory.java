package com.drujba.autobackend.models.enums.calculate;

import com.drujba.autobackend.models.enums.LocalizableEnum;

public enum CarAgeCategory implements LocalizableEnum {
    NEW("new"),
    ONE_TO_THREE("1-3"),
    THREE_TO_FIVE("3-5"),
    FIVE_TO_SEVEN("5-7"),
    OVER_SEVEN("over_7");

    private final String value;

    CarAgeCategory(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}

