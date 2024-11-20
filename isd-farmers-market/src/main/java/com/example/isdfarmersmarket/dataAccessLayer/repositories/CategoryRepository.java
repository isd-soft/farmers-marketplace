package com.example.isdfarmersmarket.dataAccessLayer.repositories;

import com.example.isdfarmersmarket.dataAccessLayer.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Integer> {
    Optional<Category> getCategoryById(Long id);

    Boolean existsByTitle(String title);
}
