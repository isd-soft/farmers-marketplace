package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.UserServiceImpl;
import com.example.isdfarmersmarket.web.commands.CustomerUpgradeCommand;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserServiceImpl userService;

    @GetMapping("/{id}")
    public UserProfileDTO getUserProfile(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserProfileDTO> getAllUsers(Pageable pageable) {
        return userService.getAllUsers(pageable);
    }

    @GetMapping("/search")
    public List<UserProfileDTO> searchUsersByFullName(@RequestParam String fullName, Pageable pageable) {
        return userService.searchUsersByFullName(fullName, pageable);
    }


}