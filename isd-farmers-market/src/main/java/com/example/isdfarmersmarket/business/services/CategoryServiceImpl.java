package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.dao.models.Category;
import com.example.isdfarmersmarket.dao.repositories.CategoryRepository;
import com.example.isdfarmersmarket.business.mapper.DaoMapper;
import com.example.isdfarmersmarket.web.dto.CategoryDTO;
import com.example.isdfarmersmarket.web.commands.CreateCategoryCommand;
import com.example.isdfarmersmarket.web.commands.UpdateCategoryCommand;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final DaoMapper daoMapper;

    @Override
    @Transactional
    public CategoryDTO createCategory(CreateCategoryCommand createCategoryCommand) {
        String title = createCategoryCommand.getTitle();
        if (categoryRepository.existsByTitle(title))
            throw new EntityExistsException("Category with the specified title already exists");
        Category category = Category.builder()
                .title(title)
                .build();
        return daoMapper.map(categoryRepository.save(category));
    }

    @Override
    @Transactional
    public CategoryDTO updateCategory(Long id, UpdateCategoryCommand updateCategoryCommand) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with the specified id not found")
                );
        String title = updateCategoryCommand.getTitle();
        if (categoryRepository.existsByTitle(title))
            throw new EntityExistsException("Category with the specified title already exists");
        category.setTitle(title);
        categoryRepository.save(category);
        return daoMapper.map(category);
    }

    @Override
    @Transactional
    public CategoryDTO deleteCategory(Long id) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with the specified id not found")
                );
        categoryRepository.delete(category);
        return daoMapper.map(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return daoMapper.mapCategories(categories);
    }
}
