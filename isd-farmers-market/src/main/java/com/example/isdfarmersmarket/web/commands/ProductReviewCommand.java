package com.example.isdfarmersmarket.web.commands;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductReviewCommand {
    Long productId;

    @Min(1)
    @Max(5)
    float rating;

    @Size(min = 1, max = 500, message = "Review content should have a size between 1 and 500 characters")
    String content;
}