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
}
