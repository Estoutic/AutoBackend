package com.drujba.autobackend.models.dto.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AuthorityDto {


    private String authority;

    public AuthorityDto(String authority) {
        this.authority = authority;
    }
}
