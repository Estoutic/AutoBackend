package com.drujba.autobackend.models.enums;

import java.util.Locale;
import java.util.ResourceBundle;

public enum CurrencyCode {
    USD,
    RUB,
    EUR,
    CNY;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("currencyCode." + this.name().toLowerCase());
    }
}

