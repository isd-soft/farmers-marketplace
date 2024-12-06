package com.example.isdfarmersmarket.web.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDTO {
    private Long id;
    private byte[] bytes;
}