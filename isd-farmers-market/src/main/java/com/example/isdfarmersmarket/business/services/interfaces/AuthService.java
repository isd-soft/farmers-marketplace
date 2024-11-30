package com.example.isdfarmersmarket.business.services.interfaces;

import com.example.isdfarmersmarket.web.commands.UpdatePasswordCommand;
import com.example.isdfarmersmarket.web.commands.UserRegisterCommand;
import com.example.isdfarmersmarket.dao.models.User;

public interface AuthService {

    User authenticate(String username, String password);

    void deleteRefreshToken(String refreshToken);

    void registerUser(UserRegisterCommand registerRequestDTO);

    String generateRefreshToken(String email);

    String generateAccessToken(String refreshToken);

    void updatePassword(UpdatePasswordCommand updatePasswordCommand);

}
