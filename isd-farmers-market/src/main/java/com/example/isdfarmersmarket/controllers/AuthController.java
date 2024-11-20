package com.example.isdfarmersmarket.controllers;

import com.example.isdfarmersmarket.DTOs.CustomerUpgradeDTO;
import com.example.isdfarmersmarket.DTOs.LoginRequestDTO;
import com.example.isdfarmersmarket.DTOs.UserRegisterRequestDTO;
import com.example.isdfarmersmarket.models.User;
import com.example.isdfarmersmarket.services.AuthService;
import com.example.isdfarmersmarket.services.JwtService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@Slf4j
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> userRegister(@RequestBody UserRegisterRequestDTO registerRequestDTO) {
        authService.registerUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

    @PreAuthorize("hasRole('CUSTOMER') and not hasRole('ADMIN')")
    @PostMapping("/customer/upgrade")
    public ResponseEntity<Map<String, String>> upgradeCustomer(@RequestBody CustomerUpgradeDTO customerUpgradeDTO,
                                                               Authentication authentication) {
        authService.upgradeUser(authentication.getName(), customerUpgradeDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "Customer upgraded successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = authService.authenticate(loginRequestDTO.email(), loginRequestDTO.password());
        String refreshToken = authService.generateRefreshToken(user.getUsername());
        String accessToken = authService.generateAccessToken(refreshToken);

        return ResponseEntity.ok(Map.of(
                "accessToken", accessToken,
                "refreshToken", refreshToken
        ));
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@Parameter(hidden = true)
                                                            @RequestHeader(value="Authorization", required = false) String authorizationHeader) {
        String oldRefreshToken = jwtService.extractTokenFromHeader(authorizationHeader);
        String newAccessToken = authService.generateAccessToken(oldRefreshToken);

        return ResponseEntity.ok(Map.of("accessToken", newAccessToken));
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@Parameter(hidden = true)
                                                      @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
        String refreshToken = jwtService.extractTokenFromHeader(authorizationHeader);
        authService.deleteRefreshToken(refreshToken);
        return ResponseEntity.ok(Map.of("message", "User logged out successfully"));
    }
}
