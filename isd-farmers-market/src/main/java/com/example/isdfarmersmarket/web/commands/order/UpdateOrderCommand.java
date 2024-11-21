package com.example.isdfarmersmarket.web.commands.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Data
public class UpdateOrderCommand {
    @NotBlank(message = "Order status cannot be blank")
    @Schema(example = "PENDING")
    private String orderStatus;

    @NotNull(message = "Total price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    @Schema(example = "99.99")
    private BigDecimal totalPrice;

//    @NotNull(message = "User ID cannot be null")
//    @Schema(example = "1")
//    private Long userId;

    @NotEmpty(message = "Products cannot be empty")
    @Size(min = 1, message = "At least one product must be included in the order")
    @Schema(description = "List of product IDs to include in the order", example = "[1, 2, 3]")
    private Set<Long> productId = new HashSet<>();

}
