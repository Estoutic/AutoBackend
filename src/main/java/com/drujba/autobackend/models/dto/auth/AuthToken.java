package com.drujba.autobackend.models.dto.auth;



import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthToken {
    private String token;

    public AuthToken(String token) {
        this.token = token;
    }

}