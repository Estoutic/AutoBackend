package com.drujba.autobackend.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder extends BCryptPasswordEncoder {

    public static PasswordEncoder passwordEncoder;

    public static PasswordEncoder getInstance() {
        if (passwordEncoder == null) {
            passwordEncoder = new PasswordEncoder();
        }
        return passwordEncoder;

    }
}


