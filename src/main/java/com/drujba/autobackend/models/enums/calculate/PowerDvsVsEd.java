package com.drujba.autobackend.models.enums.calculate;

import java.util.Locale;
import java.util.ResourceBundle;

public enum PowerDvsVsEd {
    DVS_HIGHER_ED,
    DVS_LOWER_ED;

    public String getLocalizedValue(Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        return bundle.getString("powerDvsVsEd." + this.name().toLowerCase());
    }
}

