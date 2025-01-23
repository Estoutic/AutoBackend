package com.drujba.autobackend.models.enums.application;

import java.util.Locale;
import java.util.ResourceBundle;

public enum ContactType {
    CALL,
    EMAIL,
    WHATSAPP,
    TELEGRAM;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("contactType." + this.name().toLowerCase());
    }
}
