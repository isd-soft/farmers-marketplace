package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.interfaces.UserService;
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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.OK;


@RestController
@RequestMapping("/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {

    UserService userService;

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileDTO> getUserProfile(@PathVariable Long id) {
        UserProfileDTO userProfile = userService.getUserProfile(id);
        return ResponseEntity.ok(userProfile);
    }

    @GetMapping
    public ResponseEntity<PageResponseDTO<UserProfileDTO>> searchUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) SearchUserByRoleParams roleParams,
            Pageable pageable
    ) {
        PageResponseDTO<UserProfileDTO> users = userService.searchUsers(search, roleParams, pageable);
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

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/upgrade-to-admin/{id}")
    public ResponseEntity<UserProfileDTO> upgradeToAdmin(@PathVariable Long id) {
        UserProfileDTO upgradedUser = userService.upgradeToAdmin(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(upgradedUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<UserProfileDTO> deleteUser(@PathVariable Long id) {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal.getId().equals(id)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        userService.deleteUserById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserDTO> updateUser(@PathVariable Long id, @RequestBody UpdateUserDTO updateUserDTO) {

        UpdateUserCommand updateUserCommand = new UpdateUserCommand();
        updateUserCommand.setId(id);
        updateUserCommand.setFirstName(updateUserDTO.getFirstName());
        updateUserCommand.setLastName(updateUserDTO.getLastName());
        updateUserCommand.setEmail(updateUserDTO.getEmail());
        updateUserCommand.setPhoneNumber(updateUserDTO.getPhoneNumber());
        updateUserCommand.setDescription(updateUserDTO.getDescription());
        updateUserCommand.setAddress(updateUserDTO.getAddress());

        UpdateUserDTO updatedUserDTO = userService.updateUser(updateUserCommand);

        return ResponseEntity.ok(updatedUserDTO);
    }



}
