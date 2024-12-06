package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.interfaces.ProductService;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.ProductDiscountCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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
    @PreAuthorize("hasRole('FARMER')")
    @PostMapping(value = "/create")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody CreateProductCommand createProductCommand) {
        return ResponseEntity.status(CREATED).body(productService.createProduct(createProductCommand));
    }

    @Operation(
            description = "This endpoint is used to update a product"
    )
    @PreAuthorize("hasRole('FARMER')")
    @PutMapping(value = "/update/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @RequestBody UpdateProductCommand updateProductCommand) {
        return ResponseEntity.status(OK).body(productService.updateProduct(id, updateProductCommand));
    }
    @PreAuthorize("hasRole('FARMER')")
    @PutMapping(value = "/discount/{id}")
    public ResponseEntity<ProductDTO> setDiscountProduct(@PathVariable Long id, @RequestBody ProductDiscountCommand discountPercents) {
        return ResponseEntity.status(OK).body(productService.setDiscountProduct(id, discountPercents.getDiscountPercents()));
    }

    @Operation(
            description = "This endpoint is used to delete a product"
    )
    @PreAuthorize("hasRole('FARMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(productService.deleteProduct(id));
    }

    @Operation(
            description = "This endpoint is used to get all products"
    )
    @GetMapping()
    public ResponseEntity<Page<CompactProductDTO>> getAllProducts(
            Pageable pageable,
            @RequestParam(required = false) Long category,
            @RequestParam(required = false) String search
            ) {
        return ResponseEntity.status(OK).body(productService.getAllProducts(category, search, pageable));
    }
    @GetMapping("/management")
    @PreAuthorize("hasRole('FARMER')")
    public ResponseEntity<Page<CompactProductDTO>> getCurrentUserProducts(Pageable pageable) {
        return ResponseEntity.status(OK).body(productService.getCurrentUserProducts(pageable));
    }
    @Operation(
            description = "This endpoint is used to get a product by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(productService.getProductById(id));
    }
    @Operation(
            description = "This endpoint is used to get a detailed product page by ID"
    )
    @GetMapping("/{productId}/page")
    public ResponseEntity<ProductPageDTO> getProductPage(@PathVariable Long productId) {
        return ResponseEntity.status(OK).body(productService.getProductPageById(productId));
    }
    @Operation(
            description = "This endpoint is used to get products for a certain farmer"
    )
    @GetMapping("/{farmerId}/products")
    public ResponseEntity<Page<CompactProductDTO>> getFarmersProducts(@PathVariable Long farmerId, Pageable pageable) {
        return ResponseEntity.status(OK).body(productService.getFarmersProducts(farmerId, pageable));
    }


}
