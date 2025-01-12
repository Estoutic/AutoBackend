package com.drujba.autobackend.models.enums.auto;

import java.util.Locale;
import java.util.ResourceBundle;

public enum EngineType {
    GASOLINE,
    HYBRID,
    DIESEL,
    ELECTRIC;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("engineType." + this.name().toLowerCase());
    }
}