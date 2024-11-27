package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.ProductService;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.PageResponseDTO;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import com.example.isdfarmersmarket.web.dto.ProductPageDTO;
import com.example.isdfarmersmarket.web.dto.ProductReviewDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
@Slf4j
@Controller
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @Operation(
            summary = "Create a product",
            description = "This endpoint allows creating a product with images"
    )
    @PostMapping(value = "/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductCommand createProductCommand) {
        return ResponseEntity.status(CREATED).body(productService.createProduct(createProductCommand));
    }

    @Operation(
            description = "This endpoint is used to update a product"
    )
    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ProductDTO> updateCategory(@PathVariable Long id, @RequestPart @Valid UpdateProductCommand updateProductCommand,
                                                     @RequestPart(value = "files", required = false) List<MultipartFile>files,
                                                     @RequestPart(value = "imagesToDeleteId", required = false) List<Long>imagesToDeleteId) {
        return ResponseEntity.status(OK).body(productService.updateProduct(id, updateProductCommand,new HashSet<>(files), new HashSet<>(imagesToDeleteId)));
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
    public ResponseEntity<List<ProductDTO>> getAllProducts(
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search) {
        return ResponseEntity.status(OK).body(productService.getAllProducts(category, search));
    }
    @Operation(
            description = "This endpoint is used to get a product by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(productService.getProductById(id));
    }
    @Operation(
            description = "This endpoint is used to get product reviews by id"
    )
    @GetMapping("/{productId}/reviews")
    public ResponseEntity<PageResponseDTO<ProductReviewDTO>> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int pageSize) {
        return ResponseEntity.status(OK).body(productService.getProductReviews(productId, page, pageSize));
    }
    @Operation(
            description = "This endpoint is used to get a detailed product page by ID"
    )
    @GetMapping("/{productId}/page")
    public ResponseEntity<ProductPageDTO> getProductPage(@PathVariable Long productId) {
        return ResponseEntity.status(OK).body(productService.getProductPageById(productId));
    }

}
