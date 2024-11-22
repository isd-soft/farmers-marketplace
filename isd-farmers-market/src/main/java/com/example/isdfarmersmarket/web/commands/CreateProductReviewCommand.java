package com.example.isdfarmersmarket.web.commands;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


public class CreateProductReviewCommand {

    @NotBlank(message = "Review content cannot be blank")
    @Schema(example = "Great!")
    @Size(min = 1, max = 500, message = "Review content should have a size between 1 and 500 characters")
    private String content;
    @NotBlank(message = "Review rating cannot be blank")
    @Min(value = 1, message = "Review rating should be minimum 1")
    @Max(value = 5, message = "Review rating should be maximum 5")
    private float rating;
    @NotBlank(message = "Product cannot be blank")
    private Long productId;
}
