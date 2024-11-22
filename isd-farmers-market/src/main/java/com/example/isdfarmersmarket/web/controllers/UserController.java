package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.UserService;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping
    public List<UserProfileDTO> getAllUsers(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return userService.getAllUsers(page, pageSize);
    }

    @GetMapping("/search")
    public List<UserProfileDTO> searchUsersByFullName(@RequestParam String fullName, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return userService.searchUsersByFullName(fullName, page, pageSize);
    }

    @PutMapping("/profile-picture")
    @PreAuthorize("isAuthenticated()")
    public void changeProfilePicture(@RequestParam String newPfp, Authentication authentication) {
        userService.changePfp(authentication.getName(), newPfp);
    }
}