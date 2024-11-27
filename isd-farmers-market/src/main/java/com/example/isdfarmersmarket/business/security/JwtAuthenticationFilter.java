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
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        try{
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
        }
        catch (ExpiredJwtException ex) {
            sendErrorResponse(response, AuthError.TOKEN_EXPIRED.name());

        } catch (UnsupportedJwtException ex) {
            sendErrorResponse(response, "JWT token is unsupported");

        } catch (MalformedJwtException ex) {
            sendErrorResponse(response, "JWT token is malformed");

        } catch (SignatureException ex) {
            sendErrorResponse(response, "JWT token signature validation failed");

        } catch (IllegalArgumentException ex) {
            sendErrorResponse(response, "Illegal argument during JWT processing");
        }
        catch (JwtException ex) {
            sendErrorResponse(response, "Unexpected JWT exception");
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

    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message);

        response.getWriter().write(mapper.writeValueAsString(problemDetail));
    }

}