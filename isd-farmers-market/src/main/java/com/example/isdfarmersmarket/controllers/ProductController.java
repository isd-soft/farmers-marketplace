package com.example.isdfarmersmarket.controllers;

import com.example.isdfarmersmarket.DTOs.ProductDTO;
import com.example.isdfarmersmarket.enums.UnitType;
import com.example.isdfarmersmarket.services.ImageService;
import com.example.isdfarmersmarket.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import java.util.*;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;
    @GetMapping("/")
    public ModelAndView products(){
        ModelAndView model = new ModelAndView("products");

        model.addObject("products", productService.getProducts());

        model.addObject("unitTypes", UnitType.values());
        return model;
    }
    @GetMapping("/product/{id}")
    public ModelAndView productInfo(@PathVariable Long id){
        ModelAndView model = new ModelAndView("product-info");
        ProductDTO product = productService.getProductById(id);
        product.getImages().forEach(image -> {
            String base64Image = Base64.getEncoder().encodeToString(image.getBytes());
            image.setBase64Image(base64Image);
        });
        model.addObject("product", product);
        return model;
    }
    @PostMapping("/product/create")
    public ModelAndView createProduct(@RequestParam("newImages") List<MultipartFile> newImages,
                                      ProductDTO product) {
        productService.saveProduct(product, newImages);

        return new ModelAndView("redirect:/");
    }


    @PostMapping("/product/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable  Long id){
        ModelAndView model = new ModelAndView("redirect:/");
        productService.deleteProduct(id);
        return model;
    }

}
