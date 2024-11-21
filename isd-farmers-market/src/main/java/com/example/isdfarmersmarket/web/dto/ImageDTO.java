package com.example.isdfarmersmarket.web.dto;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ImageDTO {
    private Long id;
    private String originalFileName;
    private Long size;
    private String fileType;
    private byte[] bytes;
}
