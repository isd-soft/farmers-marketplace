package com.example.isdfarmersmarket.business.security;

import com.example.isdfarmersmarket.business.services.JwtService;
import com.example.isdfarmersmarket.dao.enums.AuthError;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    ObjectMapper mapper;
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try {
            // Getting token from header
            final String token = getToken(request, response, filterChain);
            // Return if there is no token
            if (token == null) return;
            // Extracting claims
            Claims claims = jwtService.extractAllClaims(token);
            final Long id = Long.valueOf(claims.getSubject());
            final String email = claims.get("email", String.class);
            final List<String> roles = claims.get("roles", List.class);
            // Check if refresh token
            if (refreshTokenCheck(request, response, filterChain, roles)) return;
            // Setting authentication
            setAuthentication(request, id, email, roles);
            // Continue request
            filterChain.doFilter(request, response);
        } catch (JwtException ex) {
            handleJwtException(response, ex);
        }
    }

    private void setAuthentication(HttpServletRequest request, Long id, String email, List<String> roles) {
        var principal = new JwtPrincipal(
                id,
                email,
                roles.stream()
                        .map(ERole::valueOf)
                        .toList());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                principal,
                null,
                roles.stream().map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                        .toList()
        );

        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private boolean refreshTokenCheck(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain, List<String> roles) throws IOException, ServletException {
        if (roles == null) {
            filterChain.doFilter(request, response);
            return true;
        }
        return false;
    }

    private String getToken(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        final String authHeader = request.getHeader("Authorization");
        if (authHeader == null) {
            filterChain.doFilter(request, response);
            return null;
        }

        final String token = authHeader.substring(7);
        return token;
    }

    private void handleJwtException(HttpServletResponse response, JwtException ex) throws IOException {
        sendErrorResponse(response, ex);
    }

    private void sendErrorResponse(HttpServletResponse response, JwtException ex) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        String message;
        Map<String, Object> properties = Map.of();

        if (ex instanceof ExpiredJwtException) {
            message = AuthError.TOKEN_EXPIRED.name();
        } else if (ex instanceof UnsupportedJwtException) {
            message = "JWT token is unsupported";
        } else if (ex instanceof MalformedJwtException) {
            message = "JWT token is malformed";
        } else if (ex instanceof SignatureException) {
            message = "JWT token signature validation failed";
        }  else {
            message = "Unexpected JWT exception";
        }

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message);

        problemDetail.setProperties(properties);

        response.getWriter().write(mapper.writeValueAsString(problemDetail));
    }
}
