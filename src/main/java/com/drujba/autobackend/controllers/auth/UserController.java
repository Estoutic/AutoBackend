package com.drujba.autobackend.controllers.auth;

import com.drujba.autobackend.configs.auth.TokenProvider;
import com.drujba.autobackend.db.entities.auth.RefreshToken;
import com.drujba.autobackend.db.entities.auth.User;
import com.drujba.autobackend.exceptions.auth.TokenRefreshException;
import com.drujba.autobackend.models.dto.auth.AuthToken;
import com.drujba.autobackend.models.dto.auth.RefreshTokenDto;
import com.drujba.autobackend.models.dto.auth.UserDto;
import com.drujba.autobackend.services.auth.IAuthService;
import com.drujba.autobackend.services.auth.IRefreshTokenService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final TokenProvider jwtTokenUtil;
    private final IAuthService authService;
    private final IRefreshTokenService refreshTokenService;


    @PostMapping("/auth")
    public ResponseEntity<?> generateToken(@RequestBody UserDto loginUser) throws JsonProcessingException {
        final Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginUser.getEmail(),
                        loginUser.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        User user = authService.getUserByEmail(loginUser.getEmail());

        String accessToken = jwtTokenUtil.generateToken(
                user.getEmail(),
                user.getRoles().stream()
                        .map(role -> new SimpleGrantedAuthority(role.getName()))
                        .collect(Collectors.toList())
        );
        RefreshToken refreshToken = refreshTokenService.createRefreshToken(user);

        return new ResponseEntity<>(new AuthToken(accessToken, refreshToken.getToken()), HttpStatus.OK);
    }


    @PostMapping("/register")
    public ResponseEntity<UUID> saveUser(@RequestBody UserDto user) {
        return new ResponseEntity<>(authService.save(user), HttpStatus.CREATED);
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<AuthToken> refreshToken(@RequestBody RefreshTokenDto refreshToken) {
        Optional<RefreshToken> optionalRefreshToken = refreshTokenService.findByToken(refreshToken.getRefreshToken());

        if (optionalRefreshToken.isEmpty()) {
            throw new TokenRefreshException("Invalid refresh token");
        }

        RefreshToken verifiedRefreshToken = refreshTokenService.verifyExpiration(optionalRefreshToken.get());

        User user = verifiedRefreshToken.getUser();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>();
        for (var role : user.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        String newAccessToken = jwtTokenUtil.generateToken(user.getEmail(), authorities);

        AuthToken authToken = new AuthToken(newAccessToken, refreshToken.getRefreshToken());
        return ResponseEntity.ok(authToken);
    }


}
