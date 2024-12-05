package com.example.isdfarmersmarket.web.commands.cart;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddItemInCartCommand {
    Long productId;
    int quantity;
}
