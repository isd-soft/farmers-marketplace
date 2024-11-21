package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
//    Optional<User> find(String email);

    boolean existsByEmail(String email);
}
