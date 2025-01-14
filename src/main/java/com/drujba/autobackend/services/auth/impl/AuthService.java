package com.drujba.autobackend.services.auth.impl;

import com.drujba.autobackend.db.entities.auth.Role;
import com.drujba.autobackend.db.entities.auth.User;
import com.drujba.autobackend.db.repostiories.auth.UserRepository;
import com.drujba.autobackend.exceptions.auth.InvalidEmailInputException;
import com.drujba.autobackend.exceptions.auth.UserAlreadyExistException;
import com.drujba.autobackend.exceptions.auth.UserDoesNotExistException;
import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.services.auth.IAuthService;
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

    private final RoleService roleService;
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

    public List<UserDto> findAll() {
        return userRepository.findAll().stream().map(UserDto::new).toList();
    }


    @Override
    public UUID save(UserDto userDto) {

        String email = userDto.getEmail();
        if (userRepository.existsByEmail(email)) {
            throw new UserAlreadyExistException(email);
        }

        User user = new User(userDto);

        user.setPassword(passwordEncoder.encode(userDto.getPassword()));

        Role role = roleService.findByName("MANAGER");
        Set<Role> roleSet = new HashSet<>();
        roleSet.add(role);

        String emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$";
        Pattern pattern = Pattern.compile(emailRegex);
        if (!pattern.matcher(email).matches()) {
            throw new InvalidEmailInputException(email);
        }
        if (user.getEmail().split("@")[1].equals("admin.ru")) {
            role = roleService.findByName("ADMIN");
            roleSet.add(role);
        }

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
}
