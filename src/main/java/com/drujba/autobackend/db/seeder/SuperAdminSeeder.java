package com.drujba.autobackend.db.seeder;

import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.services.auth.IAuthService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SuperAdminSeeder {
    private final IAuthService authService;

    @Value("${admin.mail}")
    private String adminMail;

    @Value("${admin.password}")
    private String adminPassword;

    @PostConstruct
    public void seedSuperAdmin() {
        if (!authService.existsUserByEmail(adminMail)) {
            UserDto superAdmin = new UserDto();
            superAdmin.setEmail(adminMail);
            superAdmin.setPassword(adminPassword);
            authService.saveSuperAdmin(superAdmin);
        }
    }
}