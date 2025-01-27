package com.drujba.autobackend.models.enums.calculate;

import java.util.Locale;
import java.util.ResourceBundle;

public enum Country {
    RUSSIA,
    BELARUS;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("country." + this.name().toLowerCase());
    }
}

