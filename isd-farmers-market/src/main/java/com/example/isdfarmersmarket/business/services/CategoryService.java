package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.web.dto.CategoryDTO;
import com.example.isdfarmersmarket.web.commands.CreateCategoryCommand;
import com.example.isdfarmersmarket.web.commands.UpdateCategoryCommand;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CreateCategoryCommand createCategoryCommand);

    CategoryDTO updateCategory(Long id, UpdateCategoryCommand updateCategoryCommand);

    CategoryDTO deleteCategory(Long id);

    List<CategoryDTO> getAllCategories();
}
