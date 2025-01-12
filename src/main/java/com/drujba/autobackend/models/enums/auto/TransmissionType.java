package com.drujba.autobackend.models.enums.auto;

import java.util.Locale;
import java.util.ResourceBundle;

public enum TransmissionType {
    AUTOMATIC,
    VARIATOR,
    MECHANICAL,
    ROBOT;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("transmissionType." + this.name().toLowerCase());
    }
}
