package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.CategoryServiceImpl;
import com.example.isdfarmersmarket.web.commands.CreateCategoryCommand;
import com.example.isdfarmersmarket.web.commands.UpdateCategoryCommand;
import com.example.isdfarmersmarket.web.dto.CategoryDTO;
import com.example.isdfarmersmarket.web.dto.CategoryWithNrDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
    private final CategoryServiceImpl categoryServiceImpl;

    @Operation(
            description = "This endpoint is used to create a category"
    )
    @PostMapping()
    public ResponseEntity<CategoryDTO> createCategory(@RequestBody @Valid CreateCategoryCommand createCategoryCommand) {
        return ResponseEntity.status(CREATED).body(categoryServiceImpl.createCategory(createCategoryCommand));
    }

    @Operation(
            description = "This endpoint is used to update a category"
    )
    @PutMapping("/{id}")
    public ResponseEntity<CategoryDTO> updateCategory(@PathVariable Long id, @RequestBody @Valid UpdateCategoryCommand updateCategoryCommand) {
        return ResponseEntity.status(OK).body(categoryServiceImpl.updateCategory(id, updateCategoryCommand));
    }

    @Operation(
            description = "This endpoint is used to delete a category"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<CategoryDTO> deleteCategory(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(categoryServiceImpl.deleteCategory(id));
    }

    @Operation(
            description = "This endpoint is used to get all categories"
    )
    @GetMapping()
    public ResponseEntity<List<CategoryDTO>> getAllCategories() {
        return ResponseEntity.status(OK).body(categoryServiceImpl.getAllCategories());
    }

    @GetMapping("/stats")
    public ResponseEntity<List<CategoryWithNrDTO>> getAllCategoriesWithNr() {
        return ResponseEntity.status(OK).body(categoryServiceImpl.getAllCategoriesWithNr());
    }
}

