package com.example.isdfarmersmarket.web.commands;


import com.example.isdfarmersmarket.dao.enums.ERole;

public record UserRegisterCommand(String email, String password,
                                  String firstName, String lastName,
                                  String phoneNumber,
                                  ERole roleType,
                                  String address,
                                  String description
)
{
}
