package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.DaoMapper;
import com.example.isdfarmersmarket.business.services.interfaces.CategoryService;
import com.example.isdfarmersmarket.dao.models.Category;
import com.example.isdfarmersmarket.dao.repositories.CategoryRepository;
import com.example.isdfarmersmarket.dao.repositories.ProductRepository;
import com.example.isdfarmersmarket.web.commands.CreateCategoryCommand;
import com.example.isdfarmersmarket.web.commands.UpdateCategoryCommand;
import com.example.isdfarmersmarket.web.dto.CategoryDTO;
import com.example.isdfarmersmarket.web.dto.CategoryWithNrDTO;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {
    private final CategoryRepository categoryRepository;
    private final DaoMapper daoMapper;
    private final ProductRepository productRepository;

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
        Category category = categoryRepository.getCategoryById(id)
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
        Category category = categoryRepository.getCategoryById(id)
                .orElseThrow(() -> new EntityNotFoundException("Category with the specified id not found")
                );
        boolean hasProducts = productRepository.existsByCategoryId(id);
        if (hasProducts) {
            throw new IllegalStateException("Cannot delete category as it has associated products.");
        }
        categoryRepository.delete(category);
        return daoMapper.map(category);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryDTO> getAllCategories() {
        List<Category> categories = categoryRepository.findAll();
        return daoMapper.mapCategories(categories);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CategoryWithNrDTO> getAllCategoriesWithNr() {
        List<Category> categories = categoryRepository.findAll();
        List<Integer> nrOfItems = categories.stream()
                .map(category -> productRepository.countByCategoryId(category.getId()))
                .collect(Collectors.toList());

        return daoMapper.mapCategoriesWithNr(categories, nrOfItems); // Use the custom mapping method
    }

}