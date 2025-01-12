package com.drujba.autobackend.db.seeder;

import com.drujba.autobackend.db.entities.Role;
import com.drujba.autobackend.db.entities.User;
import com.drujba.autobackend.db.repostiories.RoleRepository;
import com.drujba.autobackend.db.repostiories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class SuperAdminSeeder implements ApplicationRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Optional<Role> superAdminRoleOptional = roleRepository.findRoleByName("SUPER_ADMIN");
        Role superAdminRole = superAdminRoleOptional.orElseGet(() -> {
            Role role = new Role();
            role.setName("SUPER_ADMIN");
            return roleRepository.save(role);
        });

        Optional<User> superAdminOptional = userRepository.findByEmail("superadmin@gamil.com");
        if (superAdminOptional.isEmpty()) {
            User superAdmin = new User();
            superAdmin.setEmail("superadmin@gamil.com");
            superAdmin.setPassword(passwordEncoder.encode("12345"));
            superAdmin.setRoles(Set.of(superAdminRole));
            superAdmin.setIs_verified(true);

            userRepository.save(superAdmin);
            System.out.println("Super Admin user created: superadmin@example.com / SuperAdmin123");
        } else {
            System.out.println("Super Admin user already exists.");
        }
    }
}
