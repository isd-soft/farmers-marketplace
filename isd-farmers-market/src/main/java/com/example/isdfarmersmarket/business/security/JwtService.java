package com.example.isdfarmersmarket.business.security;

import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Getter
    @Value("${jwt.expiration}")
    private int expiration;

    @Getter
    @Value("${jwt.refreshExpiration}")
    private int refreshExpiration;

    private Key signingKey;

    @PostConstruct
    public void init() {
        this.signingKey = Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateAccessToken(String username, Set<Role> roles) {
        List<String> roleNames = roles.stream()
                .map(Role::getAuthority)
                .toList();
        return createToken(username, Map.of("roles", roleNames), expiration);
    }

    public String generateRefreshToken(String username) {
        return createToken(username, Map.of(), refreshExpiration);
    }

    private String createToken(String username, Map<String, Object> claims, long expirationTime) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signingKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public void validateToken(String token) {
        extractAllClaims(token);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public List<String> extractRoles(String token) {
        List<String> list = extractClaim(token, claims -> claims.get("roles", List.class));
        return list;
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    public boolean hasRole(String token, ERole requiredRole) {
        return extractRoles(token).stream()
                .anyMatch(role -> role.equals(requiredRole.name()));
    }
    private Claims extractAllClaims(String token) {
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
