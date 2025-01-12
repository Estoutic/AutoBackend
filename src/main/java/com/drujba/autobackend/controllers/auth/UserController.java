package com.drujba.autobackend.controllers.auth;

import com.drujba.autobackend.configs.auth.TokenProvider;
import com.drujba.autobackend.models.dto.auth.AuthToken;
import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.services.auth.IAuthService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping( "/user")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider jwtTokenUtil;
    private final IAuthService authService;


    @PostMapping("/auth")
    public ResponseEntity<?> generateToken(@RequestBody UserDto loginUser) throws JsonProcessingException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        final String token = jwtTokenUtil.generateToken(authentication);
        return new ResponseEntity<>(new AuthToken(token), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<UUID> saveUser(@RequestBody UserDto user) {
        return new ResponseEntity<>(authService.save(user), HttpStatus.CREATED);
    }
}
