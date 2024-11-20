package com.example.isdfarmersmarket.initializer;

import com.example.isdfarmersmarket.DTOs.UserRegisterRequestDTO;
import com.example.isdfarmersmarket.enums.Role;
import com.example.isdfarmersmarket.repositories.UserRepository;
import com.example.isdfarmersmarket.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class MainAdminInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final AuthService authService;
    @Value("${admin.email}")
    private String adminEmail;

    @Value("${admin.password}")
    private String adminPassword;


    @Override
    public void run(String... args) throws Exception {
        if (!userRepository.existsByEmail(adminEmail)) {
            var adminData = new UserRegisterRequestDTO(
                    adminEmail,adminPassword,
                    "Main","Admin",
                    "skip",
                    Role.ADMIN,
                    null,null
            );
            authService.registerUser(adminData);
        }
    }
}

