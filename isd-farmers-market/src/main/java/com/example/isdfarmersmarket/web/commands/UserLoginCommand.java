package com.example.isdfarmersmarket.web.commands;

import lombok.Data;

@Data
public class UserLoginCommand {
    private String email;
    private String password;
}