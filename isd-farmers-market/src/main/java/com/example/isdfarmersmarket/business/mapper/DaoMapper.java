package com.example.isdfarmersmarket.business.mapper;

import com.example.isdfarmersmarket.dao.models.Category;
import com.example.isdfarmersmarket.web.dto.CategoryDTO;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface DaoMapper {
    CategoryDTO map(Category category);

    List<CategoryDTO> mapCategories(List<Category> categories);
}
