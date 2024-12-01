package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.services.interfaces.JwtService;
import com.example.isdfarmersmarket.dao.models.Role;
import com.example.isdfarmersmarket.dao.models.User;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Slf4j
public class JwtServiceImpl implements JwtService {

    @Value("${jwt.secret}")
    String secret;

    @Getter
    @Value("${jwt.expiration}")
    int expiration;

    @Getter
    @Value("${jwt.refreshExpiration}")
    int refreshExpiration;

    Key signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(User user) {
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getAuthority)
                .toList();
        return createToken(user.getId(),
                Map.of("roles", roleNames,
                        "email", user.getEmail()),
                expiration);
    }

    public String generateRefreshToken(User user) {
        return createToken(user.getId(),
                Map.of(),
                refreshExpiration);
    }

    private String createToken(Long userId, Map<String, Object> claims, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(String.valueOf(userId))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }


    public Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractTokenFromHeader(String authorizationHeader) {
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        } else {
            throw new MalformedJwtException("Invalid Authorization header format");
        }
    }
}
