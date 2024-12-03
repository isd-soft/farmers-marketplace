package com.example.isdfarmersmarket.web.commands;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserUpgradeCommand {
    String address;
    String description;
}