package com.drujba.autobackend.models.enums.auto;

import java.util.Locale;
import java.util.ResourceBundle;

public enum HybridType {
    NO_HYBRID,
    ELECTRO_HYBRID,
    ELECTRO_HYBRID_PHEV;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("hybridType." + this.name().toLowerCase());
    }
}