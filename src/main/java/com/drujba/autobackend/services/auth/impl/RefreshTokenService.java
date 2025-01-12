package com.drujba.autobackend.services.auth.impl;

import com.drujba.autobackend.db.entities.RefreshToken;
import com.drujba.autobackend.db.entities.User;
import com.drujba.autobackend.db.repostiories.RefreshTokenRepository;
import com.drujba.autobackend.db.repostiories.UserRepository;
import com.drujba.autobackend.exceptions.auth.TokenRefreshException;
import com.drujba.autobackend.services.auth.IRefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class RefreshTokenService implements IRefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    @Value("${jwt.refresh.token.expiration}") // срок действия (дни)
    private int refreshTokenDurationDays;


    @Override
    public RefreshToken createRefreshToken(User user) {
        Optional<RefreshToken> existingToken = refreshTokenRepository.findByUser(user);

        if (existingToken.isPresent()) {
            RefreshToken refreshToken = existingToken.get();
            refreshToken.setExpiryDate(Instant.now().plus(refreshTokenDurationDays, ChronoUnit.DAYS));
            refreshToken.setToken(UUID.randomUUID().toString());
            return refreshTokenRepository.save(refreshToken);
        }

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setUser(user);
        refreshToken.setExpiryDate(Instant.now().plus(refreshTokenDurationDays, ChronoUnit.DAYS));
        refreshToken.setToken(UUID.randomUUID().toString());
        return refreshTokenRepository.save(refreshToken);
    }


    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.getExpiryDate().isBefore(Instant.now())) {
            refreshTokenRepository.delete(token);
            throw new TokenRefreshException("Refresh token has expired. Please log in again.");
        }
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public void deleteByUser(User user) {
        refreshTokenRepository.deleteByUser(user);
    }
}
