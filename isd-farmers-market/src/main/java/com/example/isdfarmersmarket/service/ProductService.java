package com.example.isdfarmersmarket.service;

import com.example.isdfarmersmarket.models.*;
import com.example.isdfarmersmarket.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    public List<Product> saveProducts(List<Product> products) {
        return repository.saveAll(products);
    }

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product getProductById(Long id) {
        return repository.findById(id).orElse(null);
    }

    public Product getProductByTitle(String title) {
        return repository.findByTitle(title);
    }

    public String deleteProduct(Long id) {
        repository.deleteById(id);
        return "product removed!" + id;
    }

    public Product updateProduct(Product product) {
        Product existingProduct = repository.findById(product.getId()).orElse(null);
        assert existingProduct != null;
        existingProduct.setTitle(product.getTitle());
        existingProduct.setDescription(product.getDescription());
        existingProduct.setUnitType(product.getUnitType());
        existingProduct.setPricePerUnit(product.getPricePerUnit());
        existingProduct.setDiscountPercents(product.getDiscountPercents());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setCategory(product.getCategory());
        existingProduct.setUser(product.getUser());
        existingProduct.setInWishlists(product.getInWishlists());
        existingProduct.setRating(product.getRating());
        existingProduct.setImages(product.getImages());
        existingProduct.setCreatedDate(product.getCreatedDate());
        return repository.save(existingProduct);
    }
}
