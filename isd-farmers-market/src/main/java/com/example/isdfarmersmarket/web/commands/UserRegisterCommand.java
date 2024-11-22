package com.example.isdfarmersmarket.web.commands;

import com.example.isdfarmersmarket.dao.enums.ERole;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserRegisterCommand {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private ERole roleType;
    private String address;
    private String description;
}