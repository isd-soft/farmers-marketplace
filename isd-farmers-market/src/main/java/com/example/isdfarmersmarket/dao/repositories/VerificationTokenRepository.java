package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.VerificationToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    VerificationToken findByToken(String token);
}