package com.example.isdfarmersmarket.web.commands;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data

public class ProductDiscountCommand {
    @NotNull(message = "Product quantity cannot be blank")
    @Min(value = 0, message = "Product quantity should be minimum 0")
    @Max(value = 100, message = "Product quantity should be maximum 100")
    private int discountPercents;
}
