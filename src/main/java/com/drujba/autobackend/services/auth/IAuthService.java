package com.drujba.autobackend.services.auth;

import com.drujba.autobackend.db.entities.auth.User;
import com.drujba.autobackend.models.dto.auth.UserDto;

import java.util.UUID;

public interface IAuthService {

    UUID save(UserDto userDto);

    UUID saveSuperAdmin(UserDto userDto);

    User getCurrentUser();

    User getUserByEmail(String email);

    Boolean existsUserByEmail(String email);

    Boolean checkActive(String email);
}
