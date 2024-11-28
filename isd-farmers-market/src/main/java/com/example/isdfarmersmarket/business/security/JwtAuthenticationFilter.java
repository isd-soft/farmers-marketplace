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

@Component
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    ObjectMapper mapper;
    JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        final String token = extractToken(request);

        if (token == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Claims claims = jwtService.extractAllClaims(token);
            Long id = Long.valueOf(claims.getSubject());
            String email = claims.get("email", String.class);
            List<String> roles = claims.get("roles", List.class);

            if (roles != null) {
                setAuthentication(request, id, email, roles);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException | IllegalArgumentException ex) {
            sendJwtErrorResponse(response, ex);
        }
    }

    private void sendJwtErrorResponse(HttpServletResponse response, Exception ex) throws IOException {
        String message;
        HttpStatus status = HttpStatus.UNAUTHORIZED;

        message = switch (ex) {
            case ExpiredJwtException e -> AuthError.TOKEN_EXPIRED.getMessage();
            case UnsupportedJwtException e -> AuthError.UNSUPPORTED_JWT.getMessage();
            case MalformedJwtException e -> AuthError.MALFORMED_JWT.getMessage();
            case SignatureException e -> AuthError.SIGNATURE_VALIDATION_FAILED.getMessage();
            default -> AuthError.UNEXPECTED_JWT_ERROR.getMessage();
        };

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(status, message);
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(mapper.writeValueAsString(problemDetail));
    }

    private void setAuthentication(HttpServletRequest request, Long id, String email, List<String> roles) {
        var principal = new JwtPrincipal(id, email, roles.stream().map(ERole::valueOf).toList());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                principal, null, roles.stream().map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName)).toList()
        );
        authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(authToken);
    }

    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }


}