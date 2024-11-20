package com.example.isdfarmersmarket.DTOs.abstractions;

import lombok.Data;

@Data
public abstract class UserRegisterRequestDTO {
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private String phoneNumber;
}