package com.example.isdfarmersmarket.web.dto;


import com.example.isdfarmersmarket.dao.enums.ERole;

public record UserRegisterRequestDTO(String email, String password,
                                     String firstName, String lastName,
                                     String phoneNumber,
                                     ERole roleType,
                                     String address,
                                     String description
)
{
}
