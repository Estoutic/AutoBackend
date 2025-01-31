package com.drujba.autobackend;
import java.util.Locale;
import java.util.ResourceBundle;

public class ResourceBundleTest {
    public static void main(String[] args) {
        try {
            ResourceBundle bundle = ResourceBundle.getBundle("messages", new Locale("ru"));
            System.out.println("Файл найден: " + bundle.getString("transmissionType.automatic"));
        } catch (Exception e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
    }
}