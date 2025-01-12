package com.drujba.autobackend.models.enums.auto;

import java.util.Locale;
import java.util.ResourceBundle;

public enum SteeringPosition {
    LEFT,
    RIGHT;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("steeringPosition." + this.name().toLowerCase());
    }
}