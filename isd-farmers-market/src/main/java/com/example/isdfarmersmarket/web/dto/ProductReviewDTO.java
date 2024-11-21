package com.example.isdfarmersmarket.web.dto;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
public class ProductReviewDTO {
    private Long id;
    private String content;
    private float rating;
    private UserDTO creator;
    private ProductDTO product;
}

