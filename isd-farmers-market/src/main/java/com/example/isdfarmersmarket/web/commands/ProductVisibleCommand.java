package com.example.isdfarmersmarket.web.commands;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ProductVisibleCommand {
    private boolean visible;
}
