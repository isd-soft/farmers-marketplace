package com.example.isdfarmersmarket.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CategoryWithNrDTO {
    private Long id;
    private String title;
    private Integer nrOfItems;
}
