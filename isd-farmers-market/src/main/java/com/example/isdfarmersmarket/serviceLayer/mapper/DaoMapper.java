package com.example.isdfarmersmarket.serviceLayer.mapper;

import com.example.isdfarmersmarket.dataAccessLayer.models.Category;
import com.example.isdfarmersmarket.webLayer.DTOs.CategoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DaoMapper {
    CategoryDTO map(Category category);

    List<CategoryDTO> mapCategories(List<Category> categories);
}
