package com.example.isdfarmersmarket.web.commands.order;

import jakarta.validation.constraints.*;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;


@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CreateOrderCommand {
    @NotNull(message = "Delivery Type cannot be null")
    Long deliveryTypeId;

}


