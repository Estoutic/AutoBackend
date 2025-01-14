package com.drujba.autobackend.services.auth;

import com.drujba.autobackend.db.entities.auth.Role;

public interface IRoleService {
    Role findByName(String name);
}
