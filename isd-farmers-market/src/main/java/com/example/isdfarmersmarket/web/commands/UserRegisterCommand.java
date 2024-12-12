package com.example.isdfarmersmarket.web.commands;

import com.example.isdfarmersmarket.dao.enums.ERole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterCommand {

    @NotEmpty(message = "Email cannot be empty")
    @Email(message = "Email should be valid")
    String email;

    @NotEmpty(message = "Password cannot be empty")
    @Size(min = 6, message = "Password should be at least 6 characters long")
    String password;

    @NotEmpty(message = "First name cannot be empty")
    String firstName;

    @NotEmpty(message = "Last name cannot be empty")
    String lastName;

    @NotEmpty(message = "Phone number cannot be empty")
    String phoneNumber;

    @NotEmpty(message = "Role cannot be empty")
    ERole roleType;

    String address;

    String description;
}