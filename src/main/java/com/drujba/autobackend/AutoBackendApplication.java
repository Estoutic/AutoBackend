package com.drujba.autobackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AutoBackendApplication {

    public static void main(String[] args) {
        SpringApplication application = new SpringApplication(AutoBackendApplication.class);

        // Set default profile to local if not specified
//        System.setProperty("spring.profiles.active", "local");

        application.run(args);
    }

}
