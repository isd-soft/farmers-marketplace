package com.example.isdfarmersmarket.webLayer.controllers;

import com.example.isdfarmersmarket.enums.UnitType;
import com.example.isdfarmersmarket.models.Product;
import com.example.isdfarmersmarket.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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
        model.addObject("product", productService.getProductById(id));
        return model;
    }
    @PostMapping("/product/create")
    public ModelAndView createProduct(Product product){
        ModelAndView model = new ModelAndView("redirect:/");
        productService.saveProduct(product);
        return model;
    }
    @PostMapping("/product/delete/{id}")
    public ModelAndView deleteProduct(@PathVariable  Long id){
        ModelAndView model = new ModelAndView("redirect:/");
        productService.deleteProduct(id);
        return model;
    }

}
