package com.example.isdfarmersmarket.services;

import com.example.isdfarmersmarket.DTOs.ProductDTO;
import com.example.isdfarmersmarket.mappers.Mapper;
import com.example.isdfarmersmarket.models.Image;
import com.example.isdfarmersmarket.models.Product;
import com.example.isdfarmersmarket.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository repository;
    private final Mapper<Product, ProductDTO> productMapper;

    @Transactional
    public Product saveProduct(ProductDTO productDTO, List<MultipartFile> files) {

        Set<Image> images = files.stream()
                .map(file -> {
                    try {
                        return toImageEntity(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
        productDTO.setImages(images);
        Product saved = repository.save(productMapper.toEntityNew(productDTO));
        images.forEach(image -> image.setProduct(saved));
        return saved;
    }

    @Transactional(readOnly = true)
    public List<ProductDTO> getProducts() {
        List<ProductDTO> productsDTO = repository.findAll().stream()
                .map(entity -> productMapper.toDto(entity))
                .collect(Collectors.toList());
        return productsDTO;
    }

    public ProductDTO getProductById(Long id) {
        ProductDTO productDTO = productMapper.toDto(
                repository.findById(id).orElseThrow(() -> new RuntimeException("Product not found"))
        );
        return productDTO;
    }

    public ProductDTO getProductByTitle(String title) {
        ProductDTO productDTO = productMapper.toDto(
                repository.findByTitle(title).orElseThrow(() -> new RuntimeException("Product not found"))
        );
        return productDTO;
    }

    public String deleteProduct(Long id) {
        repository.deleteById(id);
        return "product removed!" + id;
    }

    public ProductDTO updateProduct(ProductDTO productDTO) {

        Product existingProduct = repository.findById(productDTO.getId()).orElse(null);
        assert existingProduct != null;
        existingProduct.setTitle(productDTO.getTitle());
        existingProduct.setDescription(productDTO.getDescription());
        existingProduct.setUnitType(productDTO.getUnitType());
        existingProduct.setPricePerUnit(productDTO.getPricePerUnit());
        existingProduct.setDiscountPercents(productDTO.getDiscountPercents());
        existingProduct.setQuantity(productDTO.getQuantity());
        existingProduct.setCategory(productDTO.getCategory());
        existingProduct.setUser(productDTO.getUser());
        existingProduct.setRating(productDTO.getRating());
        existingProduct.setImages(productDTO.getImages());

        return productMapper.toDto(repository.save(existingProduct));
    }

    private Image toImageEntity(MultipartFile file) throws IOException {

        Image image = new Image();
        image.setName(file.getName());
        image.setOriginalFileName(file.getOriginalFilename());
        image.setContentType(file.getContentType());
        image.setSize(file.getSize());
        image.setBytes(file.getBytes());
        return image;
    }
}
