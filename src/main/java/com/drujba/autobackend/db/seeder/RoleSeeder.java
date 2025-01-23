package com.drujba.autobackend.db.seeder;

import com.drujba.autobackend.db.entities.auth.Role;
import com.drujba.autobackend.db.repositories.auth.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RoleSeeder implements ApplicationRunner {

    private final RoleRepository roleRepository;

    private static final Logger log = LoggerFactory.getLogger(RoleSeeder.class);

    @Override
    public void run(ApplicationArguments args) {
        seedRoles();
        log.info("Role seeder executed on application startup");

    }

    private void seedRoles() {
        List<String> roles = new ArrayList<>();

        roles.add("MANAGER");
        roles.add("ADMIN");
        roles.add("SUPERADMIN");

        for (var role : roles) {
            if (!roleRepository.existsByName(role)) {
                Role r = Role.builder()
                        .name(role)
                        .users(new HashSet<>())
                        .build();

                this.roleRepository.save(r);

                log.info("Role '{}' successfully seeded", role);
            }
        }
    }
}
