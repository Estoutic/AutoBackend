package com.drujba.autobackend.services.auth.impl;

import com.drujba.autobackend.db.entities.auth.Role;
import com.drujba.autobackend.db.repositories.auth.RoleRepository;
import com.drujba.autobackend.exceptions.auth.RoleDoesNotExistException;
import com.drujba.autobackend.services.auth.IRoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleService implements IRoleService {

    private final RoleRepository roleRepository;

    @Override
    public Role findByName(String name) {
        return roleRepository.findRoleByName(name).orElseThrow(() -> new RoleDoesNotExistException(name));
    }

    @Override
    public List<Role> findAll(){
        return roleRepository.findAll();
    }
}
