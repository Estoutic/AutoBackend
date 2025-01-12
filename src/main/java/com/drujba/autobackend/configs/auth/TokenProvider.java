package com.drujba.autobackend.configs.auth;

import com.drujba.autobackend.models.dto.auth.UserDetailsDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@Slf4j
public class TokenProvider implements Serializable {

    @Value("${jwt.token.validity}")
    public long TOKEN_VALIDITY;

    @Value("${jwt.signing.key}")
    public String SIGNING_KEY;

    @Value("${jwt.authorities.key}")
    public String AUTHORITIES_KEY;

    public UserDetailsDto getUserDetailsFromToken(String token) {
        ObjectMapper objectMapper = new ObjectMapper();
        String userJson = getClaimFromToken(token, Claims::getSubject);

        try {
            UserDetailsDto userDetailsDto = objectMapper.readValue(userJson, UserDetailsDto.class);
            log.info(userJson);
            return userDetailsDto;
        } catch (Exception e) {
            throw new IllegalArgumentException("Ошибка при десериализации UserDetails из токена", e);
        }
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    private Claims getAllClaimsFromToken(String token) {
        JwtParser parser = Jwts.parser()
                .setSigningKey(SIGNING_KEY)
                .build();
        return parser.parseClaimsJws(token.trim()).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    public String generateToken(String email, Collection<? extends GrantedAuthority> authorities) {
        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        return Jwts.builder()
                .setSubject(email)
                .claim(AUTHORITIES_KEY, roles)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 5000))
                .signWith(SignatureAlgorithm.HS256, SIGNING_KEY)
                .compact();
    }

    public String generateToken(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        return generateToken(userDetails.getUsername(), authentication.getAuthorities());
    }


    public Boolean validateToken(String token, UserDetails userDetails) {
        final UserDetailsDto details = getUserDetailsFromToken(token);
        return (details.getUsername().equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    UsernamePasswordAuthenticationToken getAuthenticationToken(final String token, final Authentication existingAuth, final UserDetails userDetails) {

        final JwtParser jwtParser = Jwts.parser().setSigningKey(SIGNING_KEY).build();

        log.info(String.valueOf(userDetails));
        log.info(token);
        final Jws<Claims> claimsJws = jwtParser.parseClaimsJws(token);

        final Claims claims = claimsJws.getBody();

        final Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        return new UsernamePasswordAuthenticationToken(userDetails, "", authorities);
    }
}
