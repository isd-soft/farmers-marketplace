package com.example.isdfarmersmarket.services;

import com.example.isdfarmersmarket.DTOs.CustomerUpgradeDTO;
import com.example.isdfarmersmarket.DTOs.UserRegisterRequestDTO;
import com.example.isdfarmersmarket.enums.Role;
import com.example.isdfarmersmarket.models.RefreshToken;
import com.example.isdfarmersmarket.models.User;
import com.example.isdfarmersmarket.repositories.RefreshTokenRepository;
import com.example.isdfarmersmarket.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    public User authenticate(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        return (User) authentication.getPrincipal();
    }
    public void upgradeUser(String email, CustomerUpgradeDTO customerUpgradeDTO) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(()->new RuntimeException("User not found."));
        user.setRole(Role.FARMER);
        user.setAddress(customerUpgradeDTO.address());
        user.setDescription(customerUpgradeDTO.description());
        userRepository.save(user);
    }
    public void deleteRefreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found"));
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);
    }
    public void registerUser(UserRegisterRequestDTO registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.email())) {
            throw new RuntimeException("Email already in use");
        }

        String encodedPassword = passwordEncoder.encode(registerRequestDTO.password());
        User newUser = new User();
        newUser.setEmail(registerRequestDTO.email());
        newUser.setFirstName(registerRequestDTO.firstName());
        newUser.setLastName(registerRequestDTO.lastName());
        newUser.setPhoneNumber(registerRequestDTO.phoneNumber());
        newUser.setPassword(encodedPassword);
        if (registerRequestDTO.roleType().equals(Role.FARMER)) {
            newUser.setRole(Role.FARMER);
            newUser.setAddress(registerRequestDTO.address());
            newUser.setDescription(registerRequestDTO.description());
        }
        else{
            newUser.setRole(Role.CUSTOMER);
        }
        userRepository.save(newUser);
    }

    public String generateRefreshToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new RuntimeException("User not found"));

        String refreshToken = jwtService.generateRefreshToken(email);

        tokenRepository.findByUser(user).ifPresentOrElse(
                existingToken -> {
                    existingToken.setToken(refreshToken);
                    tokenRepository.save(existingToken);
                },
                () -> {
                    RefreshToken newRefreshToken = new RefreshToken();
                    newRefreshToken.setUser(user);
                    newRefreshToken.setToken(refreshToken);
                    tokenRepository.save(newRefreshToken);
                });

        return refreshToken;
    }

    public String generateAccessToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(username).orElseThrow(
                () -> new RuntimeException("User not found"));

        tokenRepository.findByUser(user).stream()
                .filter(token -> token.getToken().equals(refreshToken))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Invalid refresh token"));

        jwtService.validateToken(refreshToken);

        return jwtService.generateAccessToken(username, user.getRole());
    }
}