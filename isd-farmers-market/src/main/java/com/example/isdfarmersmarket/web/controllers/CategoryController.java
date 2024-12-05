package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.CategoryService;
import com.example.isdfarmersmarket.web.commands.CreateCategoryCommand;
import com.example.isdfarmersmarket.web.commands.UpdateCategoryCommand;
import com.example.isdfarmersmarket.web.dto.CategoryDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
            description = "This endpoint is used to create a category"
    )
    @PostMapping()
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CreateCategoryCommand createCategoryCommand) {
        return ResponseEntity.status(CREATED).body(categoryService.createCategory(createCategoryCommand));
    }

    @Operation(
            description = "This endpoint is used to update a category"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody @Valid UpdateCategoryCommand updateCategoryCommand) {
        return ResponseEntity.status(OK).body(categoryService.updateCategory(id, updateCategoryCommand));
    }

    @Operation(
            description = "This endpoint is used to delete a category"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(categoryService.deleteCategory(id));
    }

    @Operation(
            description = "This endpoint is used to get all categories"
    )
    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.status(OK).body(categoryService.getAllCategories());
    }
}

