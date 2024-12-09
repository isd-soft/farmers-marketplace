package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.models.VerificationToken;
import com.example.isdfarmersmarket.dao.repositories.VerificationTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class VerificationTokenService {

    private final VerificationTokenRepository verificationTokenRepository;

    public VerificationToken generateVerificationToken(User user) {
        String token = generateRandomToken();

        VerificationToken verificationToken = VerificationToken.create(user, token);

        return verificationTokenRepository.save(verificationToken);
    }

    private String generateRandomToken() {
        return UUID.randomUUID().toString();
    }

    public VerificationToken getVerificationToken(String token) {
        return verificationTokenRepository.findByToken(token);
    }
}

