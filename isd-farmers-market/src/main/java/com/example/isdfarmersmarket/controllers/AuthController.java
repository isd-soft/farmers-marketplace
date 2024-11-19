package com.example.isdfarmersmarket.controllers;

import com.example.isdfarmersmarket.DTOs.CustomerRegisterRequestDTO;
import com.example.isdfarmersmarket.DTOs.LoginRequestDTO;
import com.example.isdfarmersmarket.models.User;
import com.example.isdfarmersmarket.services.AuthService;
import com.example.isdfarmersmarket.services.JwtService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;
    private final JwtService jwtService;



    /**
     * Endpoint for registering a new user.
     *
     * @param registerRequestDTO The registration request containing username and password.
     * @return A response with a success message.
     */
    @PostMapping("/register")
    public ResponseEntity<Map<String, String>> register(@RequestBody CustomerRegisterRequestDTO registerRequestDTO) {
        authService.registerUser(registerRequestDTO);

        Map<String, String> response = new HashMap<>();
        response.put("message", "User registered successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }


    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody LoginRequestDTO loginRequestDTO) {
        User user = authService.authenticate(loginRequestDTO.email(), loginRequestDTO.password());
        String refreshToken = authService.generateRefreshToken(user.getUsername());
        String accessToken = authService.generateAccessToken(refreshToken);

        Map<String, String> tokens = new HashMap<>();
        tokens.put("accessToken", accessToken);
        tokens.put("refreshToken", refreshToken);
        return ResponseEntity.ok(tokens);
    }

    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refreshToken(@Parameter(hidden = true) @RequestHeader(value="Authorization",required = false) String authorizationHeader) {
        String oldRefreshToken = jwtService.extractTokenFromHeader(authorizationHeader);
        String newAccessToken = authService.generateAccessToken(oldRefreshToken);

        Map<String, String> response = new HashMap<>();
        response.put("accessToken", newAccessToken);
        return ResponseEntity.ok(response);
    }
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@Parameter(hidden = true) @RequestHeader(value = "Authorization", required = false) String authorizationHeader) {
            String refreshToken = jwtService.extractTokenFromHeader(authorizationHeader);
            authService.deleteRefreshToken(refreshToken);
            Map<String, String> response = new HashMap<>();
            response.put("message", "User logged out successfully");
            return ResponseEntity.ok(response);
    }


}
