package com.drujba.autobackend.services.admin;

import com.drujba.autobackend.models.dto.auth.UserDto;

import java.util.List;
import java.util.UUID;

public interface IAdminService {

    void deactivateEmployee(UUID id);

    List<UserDto> getAllUsers();

}
