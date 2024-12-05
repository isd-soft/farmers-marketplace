package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.interfaces.CurrentUserService;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/current-user")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class CurrentUserController {
    CurrentUserService currentUserService;
    @GetMapping("/")
    public UserProfileDTO getCurrentUserProfile() {
        return currentUserService.getCurrentUserInfo();
    }

}
