package com.example.isdfarmersmarket.dao.repositories;

import com.example.isdfarmersmarket.dao.models.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
}
