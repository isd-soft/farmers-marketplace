package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Optional<Category> getCategoryById(Long id);

    Boolean existsByTitle(String title);
}