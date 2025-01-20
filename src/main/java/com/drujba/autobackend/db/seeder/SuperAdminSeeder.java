package com.drujba.autobackend.db.seeder;

import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.services.auth.IAuthService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class SuperAdminSeeder {
    private final IAuthService authService;

    @PostConstruct
    public void seedSuperAdmin() {
        if (!authService.existsUserByEmail("superadmin@admin.com")) {
            UserDto superAdmin = new UserDto();
            superAdmin.setEmail("superadmin@admin.com");
            superAdmin.setPassword("12345");
            authService.saveSuperAdmin(superAdmin);
        }
    }
}