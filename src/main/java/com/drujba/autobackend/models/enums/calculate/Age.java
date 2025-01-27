package com.drujba.autobackend.models.enums.calculate;

import java.util.Locale;
import java.util.ResourceBundle;

public enum Age {
    NEW_UNDER_3_YEARS,
    FROM_3_TO_5_YEARS,
    FROM_5_TO_7_YEARS,
    OVER_7_YEARS;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("age." + this.name().toLowerCase());
    }
}

