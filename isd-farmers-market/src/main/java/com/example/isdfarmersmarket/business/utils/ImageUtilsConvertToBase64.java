package com.example.isdfarmersmarket.business.utils;

import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.dao.models.Image;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Base64;
import java.util.Optional;

@Component
public class ImageUtilsConvertToBase64 {
    public static String convertImageToBase64(Optional<Image> image) {
        return image.map(img -> Base64.getEncoder().encodeToString(img.getBytes())).orElse(null);
    }
}
