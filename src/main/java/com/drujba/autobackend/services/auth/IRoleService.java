package com.drujba.autobackend.services.auth;

import com.drujba.autobackend.db.entities.auth.Role;

import java.util.List;

public interface IRoleService {
    Role findByName(String name);

    List<Role> findAll();
}
