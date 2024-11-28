package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.web.commands.UserUpgradeCommand;
import com.example.isdfarmersmarket.web.commands.UserLoginCommand;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.business.services.AuthService;
import com.example.isdfarmersmarket.business.services.JwtService;
import com.example.isdfarmersmarket.web.commands.UserRegisterCommand;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthController {
    AuthService authService;
    JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> userRegister(@RequestBody UserRegisterCommand registerRequestDTO) {
        authService.registerUser(registerRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Map.of("message", "User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody UserLoginCommand userLoginCommand) {
        User user = authService.authenticate(userLoginCommand.getEmail(), userLoginCommand.getPassword());
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
