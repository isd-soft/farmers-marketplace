package com.example.isdfarmersmarket.DTOs;

import com.example.isdfarmersmarket.enums.Role;

public record UserRegisterRequestDTO(String email, String password,
                                     String firstName, String lastName,
                                     String phoneNumber,
                                     Role roleType,
                                     String address,
                                     String description
                                     ) {
}
