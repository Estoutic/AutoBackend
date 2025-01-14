//package com.drujba.autobackend.db.seeder;
//import com.drujba.autobackend.db.entities.auth.Role;
//import com.drujba.autobackend.db.entities.auth.User;
//import com.drujba.autobackend.db.repostiories.auth.RoleRepository;
//import com.drujba.autobackend.db.repostiories.auth.UserRepository;
//import lombok.RequiredArgsConstructor;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Component;
//
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//@Component
//@RequiredArgsConstructor
//public class SuperAdminSeeder implements ApplicationRunner {
//
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final PasswordEncoder passwordEncoder;
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        // Загрузка роли или её создание
//        Role superAdminRole = roleRepository.findRoleByName("SUPER_ADMIN")
//                .orElseThrow(() -> new RuntimeException("Role 'SUPER_ADMIN' not found. Please check RoleSeeder execution."));
//
//
//        // Проверка существования супер-администратора
//        if (userRepository.findByEmail("superadmin@gamil.com").isEmpty()) {
//            User superAdmin = new User();
//            superAdmin.setEmail("superadmin@gamil.com");
//            superAdmin.setPassword(passwordEncoder.encode("12345"));
//
//            // Создание множества ролей с управляемыми объектами
//            Set<Role> roles = new HashSet<>();
//            roles.add(superAdminRole);
//            superAdmin.setRoles(roles);
//
//            superAdmin.setIs_verified(true);
//
//            // Сохранение супер-администратора
//            userRepository.save(superAdmin);
//            System.out.println("Super Admin user created: superadmin@gamil.com / 12345");
//        } else {
//            System.out.println("Super Admin user already exists.");
//        }
//    }
//}
