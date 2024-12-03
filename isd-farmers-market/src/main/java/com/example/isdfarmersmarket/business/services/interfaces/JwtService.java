package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.dao.models.User;
import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateAccessToken(User user);

    String generateRefreshToken(User user);

    Claims extractAllClaims(String token);

    String extractTokenFromHeader(String authorizationHeader);

}
