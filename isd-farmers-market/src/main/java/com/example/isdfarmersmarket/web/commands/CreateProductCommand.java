package com.example.isdfarmersmarket.web.commands;

import com.example.isdfarmersmarket.dao.enums.UnitType;
import com.example.isdfarmersmarket.dao.models.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class CreateProductCommand {
    @NotBlank(message = "Product title cannot be blank")
    @Schema(example = "Fresh tomatoes")
    @Size(min = 1, max = 80, message = "Product title should have a size between 1 and 80 characters")
    private String title;
    @NotBlank(message = "Product description cannot be blank")
    @Schema(example = "Fresh tomatoes of the best quality")
    @Size(min = 1, max = 1000, message = "Product title should have a size between 1 and 1000 characters")
    private String description;
    @NotBlank(message = "Product unit type cannot be blank")
    private UnitType unitType;
    @NotBlank(message = "Product price cannot be blank")
    @Min(value = 1, message = "Product price should be minimum 1")
    private BigDecimal pricePerUnit;
    private int discountPercents;
    @NotBlank(message = "Product quantity cannot be blank")
    @Min(value = 0, message = "Product quantity should be minimum 0")
    private int quantity;
    @NotBlank(message = "Product category cannot be blank")
    private Long categoryId;
}
