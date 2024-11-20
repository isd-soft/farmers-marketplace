package com.example.isdfarmersmarket.services;

import com.example.isdfarmersmarket.repositories.ImageRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@RequiredArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;
}
