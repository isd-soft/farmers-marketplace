package com.example.isdfarmersmarket.business.initializers;

import com.example.isdfarmersmarket.business.services.AuthService;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.UserRegisterCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Order(2)
@RequiredArgsConstructor
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
            var adminData = new UserRegisterCommand(
                    adminEmail,adminPassword,
                    "Main","Admin",
                    "skip",
                    ERole.ADMIN,
                    null,null
            );
            authService.registerUser(adminData);
        }
    }
}

