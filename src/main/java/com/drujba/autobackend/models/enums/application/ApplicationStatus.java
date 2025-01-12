package com.drujba.autobackend.models.enums.application;

import java.util.Locale;
import java.util.ResourceBundle;

public enum ApplicationStatus {
    IN_PROGRESS,
    ACCEPTED,
    REJECTED,
    COMPLETED;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("applicationStatus." + this.name().toLowerCase());
    }
}