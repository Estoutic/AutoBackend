package com.drujba.autobackend.controllers.admin;

import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.services.admin.IAdminService;
import com.drujba.autobackend.services.auth.IAuthService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final IAuthService authService;
    private final IAdminService adminService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/user/create")
    public ResponseEntity<UUID> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.save(userDto));
    }

    @PreAuthorize("hasAnyRole('ADMIN','SUPERADMIN')")
    @PutMapping("/user/{id}/deactivate")
    public ResponseEntity<Void> deactivateUser(@PathVariable UUID id, HttpServletRequest request) {
        adminService.deactivateEmployee(id);
        return ResponseEntity.noContent().build();
    }
}