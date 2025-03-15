package com.drujba.autobackend.models.enums.calculate;

import com.drujba.autobackend.exceptions.calculator.WrongParamException;

public enum VehicleOwnerType {
    INDIVIDUAL("individual"),
    COMPANY("company");

    private final String value;

    VehicleOwnerType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static VehicleOwnerType fromString(String value) throws WrongParamException {
        for (VehicleOwnerType type : VehicleOwnerType.values()) {
            if (type.value.equalsIgnoreCase(value)) {
                return type;
            }
        }
        throw new WrongParamException("Invalid owner type: " + value);
    }
}