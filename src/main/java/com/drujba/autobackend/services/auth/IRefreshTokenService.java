package com.drujba.autobackend.services.auth;

import com.drujba.autobackend.db.entities.RefreshToken;
import com.drujba.autobackend.db.entities.User;

import java.util.Optional;

public interface IRefreshTokenService {
    RefreshToken createRefreshToken(User user);

    RefreshToken verifyExpiration(RefreshToken token);

    Optional<RefreshToken> findByToken(String token);

    void deleteByUser(User user);
}
