package com.drujba.autobackend.db.repostiories.auth;

import com.drujba.autobackend.db.entities.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {

    Optional<Role> findRoleByName(String name);

    Boolean existsByName(String name);
}
