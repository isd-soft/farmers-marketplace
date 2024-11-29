package com.example.isdfarmersmarket.web.commands;

import com.example.isdfarmersmarket.dao.enums.UnitType;
import com.example.isdfarmersmarket.dao.models.Image;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Data
public class UpdateProductCommand {
    @NotBlank(message = "Product description cannot be blank")
    @Schema(example = "Fresh tomatoes of the best quality")
    @Size(min = 1, max = 1000, message = "Product title should have a size between 1 and 1000 characters")
    private String description;
    @NotNull(message = "Product unit type cannot be blank")
    private UnitType unitType;
    @NotNull(message = "Product price cannot be blank")
    @Min(value = 1, message = "Product price should be minimum 1")
    private BigDecimal pricePerUnit;
    @NotNull(message = "Product quantity cannot be blank")
    @Min(value = 0, message = "Product quantity should be minimum 0")
    private Integer quantity;
    List<String> imagesBase64 = new ArrayList<>();
}
