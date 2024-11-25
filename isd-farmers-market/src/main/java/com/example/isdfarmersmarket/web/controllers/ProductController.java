package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.ProductService;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Set;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(
            responses = @ApiResponse(
                    description = "Created",
                    responseCode = "201"
            ),
            description = "This endpoint is used to create a product"
    )
    @PostMapping()
    public ResponseEntity<ProductDTO> createProduct(@RequestBody @Valid CreateProductCommand createProductCommand,
    @RequestParam("files") Set<MultipartFile>files) {
        return ResponseEntity.status(CREATED).body(productService.createProduct(createProductCommand, files));
    }

    @Operation(
            description = "This endpoint is used to update a product"
    )
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateCategory(@PathVariable Long id, @RequestBody @Valid UpdateProductCommand updateProductCommand,
                                                     @RequestParam("files") Set<MultipartFile>files, @RequestParam("imagesToDeleteId") Set<Long>imagesToDeleteId) {
        return ResponseEntity.status(OK).body(productService.updateProduct(id, updateProductCommand, files, imagesToDeleteId));
    }

    @Operation(
            description = "This endpoint is used to delete a product"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(productService.deleteProduct(id));
    }

    @Operation(
            description = "This endpoint is used to get all products"
    )
    @GetMapping()
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        return ResponseEntity.status(OK).body(productService.getAllProducts());
    }
    @Operation(
            description = "This endpoint is used to get a product by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(productService.getProductById(id));
    }


}
