package com.drujba.autobackend.services.admin.impl;

import com.drujba.autobackend.db.entities.auth.User;
import com.drujba.autobackend.db.repositories.auth.UserRepository;
import com.drujba.autobackend.exceptions.auth.UserDoesNotExistException;
import com.drujba.autobackend.services.admin.IAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AdminService implements IAdminService {

    private final UserRepository userRepository;

    @Value("${admin.mail}")
    private String adminMail;

    @Override
    public void deactivateEmployee(UUID id) {
        User user = userRepository.findById(id).orElseThrow(() -> new UserDoesNotExistException(id.toString()));
        if (user.getEmail().equals(adminMail)){
            return;
        }
        user.setIsActive(false);
        userRepository.save(user);
    }
}
