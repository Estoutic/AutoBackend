package com.drujba.autobackend.models.enums.auto;

import java.util.Locale;
import java.util.ResourceBundle;

public enum BodyType {
    SUV_3_DOORS,
    SUV_5_DOORS,
    SEDAN,
    COUPE,
    HATCHBACK_3_DOORS,
    HATCHBACK_5_DOORS,
    PICKUP_DOUBLE_CAB,
    PICKUP_SINGLE_CAB,
    VAN,
    MINIVAN;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("bodyType." + this.name().toLowerCase());
    }
}
