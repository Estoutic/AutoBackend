package com.drujba.autobackend.services.auth;

import com.drujba.autobackend.db.entities.User;
import com.drujba.autobackend.models.dto.auth.UserDto;

import java.util.UUID;

public interface IAuthService {

    UUID save(UserDto userDto);

    User getCurrentUser();

    User getUserByEmail(String email);
}
