package com.drujba.autobackend.controllers.admin;

import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.services.auth.IAuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {
    private final IAuthService authService;

    @PreAuthorize("hasRole('SUPERADMIN')")
    @PostMapping("/create-user")
    public ResponseEntity<UUID> createUser(@RequestBody UserDto userDto) {
        return ResponseEntity.ok(authService.save(userDto));
    }
}