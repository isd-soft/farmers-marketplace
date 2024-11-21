package com.example.isdfarmersmarket.web.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestRolesController {
    private final Logger logger = LoggerFactory.getLogger(TestRolesController.class);

    @PreAuthorize("hasRole('CUSTOMER') or hasRole('FARMER')")
    @GetMapping("/customer")
    public String testCustomer(Authentication authentication) {
        String username = authentication.getName();
        logger.info("Authenticated username: {}", username);

        authentication.getAuthorities().forEach(authority ->
                logger.info("Role: {}", authority.getAuthority())
        );

        return "SUCCESS";
    }

    @GetMapping("/authenticated")
    public String authenticated() {
        return "SUCCESS";
    }
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public String testAdmin(Authentication authentication) {
        String username = authentication.getName();
        logger.info("Authenticated username: {}", username);

        authentication.getAuthorities().forEach(authority ->
                logger.info("Role: {}", authority.getAuthority())
        );

        return "SUCCESS";
    }
    @PreAuthorize("hasRole('FARMER')")
    @GetMapping("/farmer")
    public String testFarmer(Authentication authentication) {
        String username = authentication.getName();
        logger.info("Authenticated username: {}", username);

        authentication.getAuthorities().forEach(authority ->
                logger.info("Role: {}", authority.getAuthority())
        );

        return "SUCCESS";
    }
}