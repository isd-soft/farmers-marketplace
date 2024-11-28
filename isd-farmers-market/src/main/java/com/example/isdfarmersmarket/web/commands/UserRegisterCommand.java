package com.example.isdfarmersmarket.web.commands;

import com.example.isdfarmersmarket.dao.enums.ERole;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@AllArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserRegisterCommand {
    String email;
    String password;
    String firstName;
    String lastName;
    String phoneNumber;
    ERole roleType;
    String address;
    String description;
}