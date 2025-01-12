package com.drujba.autobackend.models.enums;

public enum Locale {
    RU("ru"),
    EU("eu"),
    ZH("zh");

    private final String title;

    Locale(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }
}
