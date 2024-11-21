package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository  extends JpaRepository<Role, Long> {
    Optional<Role> findByRole(ERole role);
}
