package com.drujba.autobackend.configs.auth;

import com.drujba.autobackend.exceptions.auth.JwtAuthenticationException;
import com.drujba.autobackend.models.dto.auth.UserDetailsDto;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import jakarta.annotation.Resource;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value("${jwt.header.string}")
    public String HEADER_STRING;

    @Value("${jwt.token.prefix}")
    public String TOKEN_PREFIX;

    @Resource(name = "userService")
    private UserDetailsService userDetailsService;

    @Autowired
    private TokenProvider jwtTokenUtil;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.equals("/users/register") ||
                path.equals("/users/auth") ||
                path.equals("/main/version") ||
                path.equals("/api/users/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        String path = req.getRequestURI();
        logger.info("Processing request for path: " + path);

        if (shouldNotFilter(req)) {
            logger.info("Skipping JWT filter for path: " + path);
            chain.doFilter(req, res);
            return;
        }

        String header = req.getHeader(HEADER_STRING);
        UserDetailsDto details = null;
        String authToken = null;

        try {


            if (header != null && header.startsWith(TOKEN_PREFIX)) {
                authToken = header.replace(TOKEN_PREFIX, "").trim();


                try {
                    details = jwtTokenUtil.getUserDetailsFromToken(authToken);
                } catch (IllegalArgumentException e) {
                    throw new JwtAuthenticationException("Invalid JWT token", HttpServletResponse.SC_BAD_REQUEST);
                } catch (ExpiredJwtException e) {
                    throw new JwtAuthenticationException("JWT token has expired", HttpServletResponse.SC_UNAUTHORIZED);
                } catch (SignatureException e) {
                    throw new JwtAuthenticationException("Invalid JWT signature", HttpServletResponse.SC_UNAUTHORIZED);
                }

            } else {
                logger.info("No Authorization header found. Proceeding without authentication.");
            }

            if (details != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(details.getUsername());

                if (jwtTokenUtil.validateToken(authToken, userDetails)) {
                    UsernamePasswordAuthenticationToken authentication = jwtTokenUtil.getAuthenticationToken(authToken, SecurityContextHolder.getContext().getAuthentication(), userDetails);
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
                    logger.info("User authenticated: " + details + ", setting security context");
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }

            chain.doFilter(req, res);
        }catch (JwtAuthenticationException e) {
            res.setStatus(e.getHttpStatus());
            res.setContentType("application/json");
            res.getWriter().write(String.format("{\"error\": \"%s\"}", e.getMessage()));
        }
    }
}
