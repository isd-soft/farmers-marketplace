package com.example.isdfarmersmarket.web.commands.order;

import com.example.isdfarmersmarket.dao.enums.OrderStatus;
import com.example.isdfarmersmarket.dao.models.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Data
public class CreateOrderCommand {
    @NotNull(message = "Order Status cannot be null")
    @Schema(example = "PENDING")
    private OrderStatus orderStatus;
    @NotNull(message = "User ID cannot be null")
    @Schema(example = "1")
    private Long userId;

@NotNull(message = "Total price cannot be null")
    @DecimalMin(value = "0.0", inclusive = false, message = "Total price must be greater than 0")
    @Schema(example = "99.99")
    private BigDecimal totalPrice;

    @NotEmpty(message = "Products cannot be empty")
    @Schema(description = "List of products and their quantities", example = "[{\"id\": 1, \"quantity\": 2}, {\"id\": 2, \"quantity\": 3}]")
    private Set<ProductItem> productsId;

    private LocalDateTime createdDate = LocalDateTime.now();

    @Data
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ProductItem {
        @NotNull(message = "Product ID cannot be null")
        @Schema(example = "1")
        private Long id;

        @Min(value = 1, message = "Quantity must be at least 1")
        @Schema(example = "2")
        private int quantity;

        private Long orderId;
    }
}


