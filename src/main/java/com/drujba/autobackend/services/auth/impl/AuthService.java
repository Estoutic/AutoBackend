package com.drujba.autobackend.services.auth.impl;

import com.drujba.autobackend.db.entities.auth.Role;
import com.drujba.autobackend.db.entities.auth.User;
import com.drujba.autobackend.db.repositories.auth.UserRepository;
import com.drujba.autobackend.exceptions.auth.InvalidEmailInputException;
import com.drujba.autobackend.exceptions.auth.UserAlreadyExistException;
import com.drujba.autobackend.exceptions.auth.UserDoesNotExistException;
import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.services.auth.IAuthService;
import com.drujba.autobackend.services.auth.IRoleService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.regex.Pattern;

@Service(value = "userService")
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService, UserDetailsService {

    private final IRoleService roleService;
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserDoesNotExistException(email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), getAuthority(user));
    }

    private Set<SimpleGrantedAuthority> getAuthority(User user) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority("ROLE_" + role.getName()));
        });
        return authorities;
    }

    @Override
    public UUID save(UserDto userDto) {

        String email = userDto.getEmail();
        if (userRepository.existsUserByEmail(email)) {
            throw new UserAlreadyExistException(email);
        }

        User user = new User(userDto);
        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new InvalidEmailInputException(email);
        }

        Set<Role> roleSet = new HashSet<>();
        if (userDto.getRoles() != null && !userDto.getRoles().isEmpty()) {
            List<String> roles = userDto.getRoles();
            if (roles.contains("ADMIN")) {
                roleSet.add(roleService.findByName("ADMIN"));
            }
            roleSet.add(roleService.findByName("MANAGER"));
        }

//        if (user.getEmail().split("@")[1].equals("admin.ru")) {
//            role = roleService.findByName("ADMIN");
//            roleSet.add(role);
//        }

        user.setRoles(roleSet);
        return userRepository.save(user).getId();
    }

    @Override
    @Transactional
    public UUID saveSuperAdmin(UserDto userDto) {
        User user = new User(userDto);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        Set<Role> roleSet = new HashSet<>(roleService.findAll());
        user.setRoles(roleSet);
        return userRepository.save(user).getId();
    }

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AccessDeniedException("User is not authenticated");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new UserDoesNotExistException(email));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new UserDoesNotExistException(email));
    }

    @Override
    public Boolean existsUserByEmail(String email) {
        return userRepository.existsUserByEmail(email);
    }

    @Override
    public Boolean checkActive(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserDoesNotExistException(email));
        return user.getIsActive();
    }
}
