package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.exception.custom_exceptions.*;
import com.example.isdfarmersmarket.business.mapper.RegisterCommandMapper;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.RefreshToken;
import com.example.isdfarmersmarket.dao.models.Role;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.RefreshTokenRepository;
import com.example.isdfarmersmarket.dao.repositories.RoleRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.UserUpgradeCommand;
import com.example.isdfarmersmarket.web.commands.UserRegisterCommand;
import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final RegisterCommandMapper registerCommandMapper;

    @Transactional(readOnly = true )
    public User authenticate(String username, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));
            return (User) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    @Transactional
    public void upgradeUser(String email, UserUpgradeCommand customerUpgradeCommand) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        Role farmer = roleRepository.findByRole(ERole.FARMER).orElseThrow(
                () -> new RuntimeException("ROLE DOESNT EXIST"));
        if(user.getRoles().contains(farmer)) {
            throw new RoleAlreadyExistsException("Role already exists");
        }
        user.setAddress(customerUpgradeCommand.address());
        user.setDescription(customerUpgradeCommand.description());


        user.addRole(farmer);
        userRepository.save(user);
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        String email = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);
    }

    @Transactional
    public void registerUser(UserRegisterCommand registerRequestDTO) {
        if (userRepository.existsByEmail(registerRequestDTO.email())) {
            throw new EmailAlreadyExistsException("Email already in use");
        }
        User newUser = registerCommandMapper.map(registerRequestDTO);
        userRepository.save(newUser);
    }

    @Transactional
    public String generateRefreshToken(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

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

    @Transactional(readOnly = true)
    public String generateAccessToken(String refreshToken) {
        String username = jwtService.extractUsername(refreshToken);

        User user = userRepository.findByEmail(username)
                .orElseThrow(() -> new InvalidCredentialsException("User not found"));

        tokenRepository.findByUser(user).stream()
                .filter(token -> token.getToken().equals(refreshToken))
                .findFirst()
                .orElseThrow(() -> new RefreshTokenException("Refresh token doesn't exist"));

        try {
            jwtService.validateToken(refreshToken);
        }
        catch (JwtException ex) {
            throw new RefreshTokenException("Invalid refresh token");
        }
        return jwtService.generateAccessToken(username, user.getRoles());
    }
}
