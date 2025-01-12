package com.drujba.autobackend.models.dto.auth;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthToken {
    private String token;
    private String refreshToken;

    public AuthToken(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

}