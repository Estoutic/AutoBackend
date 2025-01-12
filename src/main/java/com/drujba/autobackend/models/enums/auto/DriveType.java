package com.drujba.autobackend.models.enums.auto;

import java.util.Locale;
import java.util.ResourceBundle;

public enum DriveType {
    FWD,
    RWD,
    AWD;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("driveType." + this.name().toLowerCase());
    }
}