package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.events.RegistrationCompleteEvent;
import com.example.isdfarmersmarket.business.exception.custom_exceptions.AccountNotActivatedException;
import com.example.isdfarmersmarket.business.exception.custom_exceptions.InvalidCredentialsException;
import com.example.isdfarmersmarket.business.exception.custom_exceptions.RefreshTokenException;
import com.example.isdfarmersmarket.business.mapper.RegisterCommandMapper;
import com.example.isdfarmersmarket.business.services.interfaces.AuthService;
import com.example.isdfarmersmarket.dao.models.RefreshToken;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.models.VerificationToken;
import com.example.isdfarmersmarket.dao.repositories.RefreshTokenRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.UpdatePasswordCommand;
import com.example.isdfarmersmarket.web.commands.UserRegisterCommand;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
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

import java.util.Date;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final JwtServiceImpl jwtService;
    private final UserRepository userRepository;
    private final RefreshTokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final RegisterCommandMapper registerCommandMapper;
    private final EventPublisher eventPublisher;
    private final VerificationTokenService verificationTokenService;

    @Transactional(readOnly = true)
    public User authenticate(String username, String password) {
        try {
            User user = userRepository.findByEmail(username)
                    .orElseThrow(() -> new EntityNotFoundException("User with the specified email not found."));

            if (!user.isEnabled()) {
                throw new AccountNotActivatedException();
            }

            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(username, password));

            return (User) authentication.getPrincipal();
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException();
        }
    }

    @Transactional
    public void deleteRefreshToken(String refreshToken) {
        Long id = Long.valueOf(jwtService
                .extractAllClaims(refreshToken)
                .getSubject());
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User not found."));
        tokenRepository.findByUser(user).ifPresent(tokenRepository::delete);
    }

    @Transactional
    public void registerUser(UserRegisterCommand registerRequestDTO, String appUrl) {

        if (userRepository.existsByEmail(registerRequestDTO.getEmail())) {
            throw new EntityExistsException("An account for the given email already exists.");
        }

        User newUser = registerCommandMapper.map(registerRequestDTO);
        userRepository.save(newUser);

        if (appUrl != null) {
            RegistrationCompleteEvent event = new RegistrationCompleteEvent(newUser, appUrl);
            eventPublisher.publishEvent(event);
        }
    }

    @Transactional
    public void validateAndEnableUser(String token) {

        VerificationToken verificationToken = verificationTokenService.getVerificationToken(token);

        if (verificationToken == null) {
            throw new EntityNotFoundException("Invalid token.");
        }

        if (verificationToken.getExpiryDate().before(new Date())) {
            throw new EntityNotFoundException("Token has expired.");
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);
    }

    @Transactional
    public String generateRefreshToken(Long id) {
        User user = userRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);

        String refreshToken = jwtService.generateRefreshToken(user);

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
        Long id = Long.valueOf(jwtService
                .extractAllClaims(refreshToken)
                .getSubject());

        User user = userRepository.findById(id)
                .orElseThrow(InvalidCredentialsException::new);

        tokenRepository.findByUserAndToken(user, refreshToken)
                .orElseThrow(RefreshTokenException::new);

        return jwtService.generateAccessToken(user);
    }

    @Transactional
    public void updatePassword(UpdatePasswordCommand updatePasswordCommand) {
        User user = userRepository.findById(updatePasswordCommand.getId())
                .orElseThrow(() -> new EntityNotFoundException("User with the specified ID not found"));

        if (!passwordEncoder.matches(updatePasswordCommand.getOldPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Old password is incorrect");
        }
        user.setPassword(passwordEncoder.encode(updatePasswordCommand.getPassword()));
        userRepository.save(user);
    }
}
