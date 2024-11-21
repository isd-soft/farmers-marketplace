package com.example.isdfarmersmarket.business.security;


import com.example.isdfarmersmarket.business.services.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

@AllArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final ObjectMapper mapper;
    private final JwtService jwtService;
    private final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try{
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                filterChain.doFilter(request, response);
                return;
            }

            final String token = authHeader.substring(7);

            final String username = jwtService.extractUsername(token);
            final List<String> roles = jwtService.extractRoles(token);

            if (roles == null) {
                filterChain.doFilter(request, response);
                return;
            }

            jwtService.validateToken(token);

            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                    username,
                    null,
                    roles.stream().map(roleName -> new SimpleGrantedAuthority("ROLE_" + roleName))
                                    .toList()
            );

            authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authToken);
            filterChain.doFilter(request, response);
        }
        catch (ExpiredJwtException ex) {
            sendErrorResponse(response, "JWT token has expired");

        } catch (UnsupportedJwtException ex) {
            sendErrorResponse(response, "JWT token is unsupported");

        } catch (MalformedJwtException ex) {
            sendErrorResponse(response, "JWT token is malformed");

        } catch (SignatureException ex) {
            sendErrorResponse(response, "JWT token signature validation failed");

        } catch (IllegalArgumentException ex) {
            sendErrorResponse(response, "Illegal argument during JWT processing");
        } catch (Exception ex) {
            sendErrorResponse(response, "An unexpected error occurred during JWT validation");
        }

    }
    private void sendErrorResponse(HttpServletResponse response, String message) throws IOException {
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.setContentType("application/json");

        ProblemDetail problemDetail = ProblemDetail.forStatusAndDetail(HttpStatus.UNAUTHORIZED, message);

        response.getWriter().write(mapper.writeValueAsString(problemDetail));
    }

}