package com.example.isdfarmersmarket.web.dto;


import com.example.isdfarmersmarket.dao.enums.Role;

public record UserRegisterRequestDTO(String email, String password,
                                     String firstName, String lastName,
                                     String phoneNumber,
                                     Role roleType,
                                     String address,
                                     String description
)
{
}
