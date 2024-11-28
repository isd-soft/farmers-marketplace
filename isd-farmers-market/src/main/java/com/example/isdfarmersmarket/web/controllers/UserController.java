package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.UserService;
import com.example.isdfarmersmarket.web.commands.UpdateUserCommand;
import com.example.isdfarmersmarket.web.dto.UpdateUserDTO;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping("/{id}")
    public UserProfileDTO getUserProfile(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @GetMapping
    public List<UserProfileDTO> getAllUsers(@RequestParam Integer page, @RequestParam Integer pageSize) {
        return userService.getAllUsers(page, pageSize);
    }

    @GetMapping("/search")
    public List<UserProfileDTO> searchUsersByFullName(@RequestParam String fullName, @RequestParam Integer page, @RequestParam Integer pageSize) {
        return userService.searchUsersByFullName(fullName, page, pageSize);
    }

    @PutMapping()
    public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody @Valid UpdateUserCommand updateUserCommand) {
        return ResponseEntity.status(OK).body(userService.updateUser(updateUserCommand));
    }


}