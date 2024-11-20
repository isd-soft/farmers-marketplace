package com.example.isdfarmersmarket.webLayer.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateCategoryCommand {

    @NotBlank(message = "Category title cannot be blank")
    @Schema(example = "Fruits")
    @Size(min = 1, max = 30, message = "Category title should have a size between 1 and 30 characters")
    private String title;
}

