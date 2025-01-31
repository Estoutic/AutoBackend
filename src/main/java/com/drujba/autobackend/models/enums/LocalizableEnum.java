package com.drujba.autobackend.models.enums;

import java.util.ResourceBundle;

public interface LocalizableEnum {
    default String getLocalizedValue(Locale customLocale) {
        java.util.Locale javaLocale = new java.util.Locale(customLocale.getTitle());
        ResourceBundle bundle = ResourceBundle.getBundle("messages", javaLocale);

        String className = this.getClass().getSimpleName();
        String camelCaseClassName = className.substring(0, 1).toLowerCase() + className.substring(1);

        return bundle.getString(camelCaseClassName + "." + ((Enum<?>) this).name().toLowerCase());
    }
}