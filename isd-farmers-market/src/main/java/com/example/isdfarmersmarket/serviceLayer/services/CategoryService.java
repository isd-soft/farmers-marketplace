package com.example.isdfarmersmarket.serviceLayer.services;

import com.example.isdfarmersmarket.webLayer.DTOs.CategoryDTO;
import com.example.isdfarmersmarket.webLayer.commands.CreateCategoryCommand;
import com.example.isdfarmersmarket.webLayer.commands.UpdateCategoryCommand;

import java.util.List;

public interface CategoryService {
    CategoryDTO createCategory(CreateCategoryCommand createCategoryCommand);

    CategoryDTO updateCategory(Long id, UpdateCategoryCommand updateCategoryCommand);

    CategoryDTO deleteCategory(Long id);

    List<CategoryDTO> getAllCategories();
}
