package com.example.isdfarmersmarket.web.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.List;

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PageResponseDTO<T> {
    List<T> content;
    Long totalElements;
    Integer pageNumber;
    Integer pageSize;
}
