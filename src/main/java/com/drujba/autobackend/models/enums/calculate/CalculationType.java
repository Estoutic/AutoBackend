package com.drujba.autobackend.models.enums.calculate;

import java.util.Locale;
import java.util.ResourceBundle;

public enum CalculationType {

    INDIVIDUAL,
    PERSONAL_INDIVIDUAL,
    LEGAL;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("calculationType." + this.name().toLowerCase());
    }
}

