package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.UserServiceImpl;
import com.example.isdfarmersmarket.dao.enums.SearchUserByRoleParams;
import com.example.isdfarmersmarket.web.commands.CustomerUpgradeCommand;
import com.example.isdfarmersmarket.web.commands.UpdateUserCommand;
import com.example.isdfarmersmarket.web.dto.PageResponseDTO;
import com.example.isdfarmersmarket.web.dto.UpdateUserDTO;
import com.example.isdfarmersmarket.web.dto.UserProfileDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserServiceImpl userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        UserProfileDTO userProfile = userService.getUserById(id);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping("/search")
    public ResponseEntity<PageResponseDTO<UserProfileDTO>> searchUsers(
            @RequestParam(required = false) String fullName,
            @RequestParam(required = false) SearchUserByRoleParams roleParams,
            Pageable pageable
    ) {
        PageResponseDTO<UserProfileDTO> users = userService.searchUsers(fullName, roleParams, pageable);
        return ResponseEntity.ok(users);
    }

    @PutMapping()
    public ResponseEntity<UpdateUserDTO> updateUser(@RequestBody @Valid UpdateUserCommand updateUserCommand) {
        return ResponseEntity.status(OK).body(userService.updateUser(updateUserCommand));
    }

    @PostMapping("/upgrade-to-farmer")
    public ResponseEntity<UserProfileDTO> upgradeToFarmer(@RequestBody CustomerUpgradeCommand command) {
        UserProfileDTO upgradedUser = userService.upgradeToFarmer(command);
        return ResponseEntity.status(HttpStatus.CREATED).body(upgradedUser);
    }
}
