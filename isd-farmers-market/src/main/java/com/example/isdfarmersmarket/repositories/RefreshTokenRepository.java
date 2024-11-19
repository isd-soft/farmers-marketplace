package com.example.isdfarmersmarket.repositories;

import com.example.isdfarmersmarket.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.isdfarmersmarket.models.User;

import java.util.Optional;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByUser(User user);
}
