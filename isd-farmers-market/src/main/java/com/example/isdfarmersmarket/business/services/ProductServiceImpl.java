package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.ProductMapper;
import com.example.isdfarmersmarket.dao.models.Category;
import com.example.isdfarmersmarket.dao.models.Image;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.repositories.CategoryRepository;
import com.example.isdfarmersmarket.dao.repositories.ImageRepository;
import com.example.isdfarmersmarket.dao.repositories.ProductRepository;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private static final String PRODUCT_FIND_FAILED_BY_ID = "Product with the specified id not found";
    private static final String CATEGORY_FIND_FAILED_BY_ID = "Category with the specified id not found";

    @Override
    @Transactional
    public ProductDTO createProduct(CreateProductCommand createProductCommand, Set<MultipartFile> files) {
        Set<Image> images = new HashSet<>();
        files.forEach(file -> {
            Image image = null;
            try {
                image = toImageEntity(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            images.add(image);
        });

        Category category = categoryRepository.getCategoryById(createProductCommand.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_FIND_FAILED_BY_ID));
        Product product = Product.builder()
                .title(createProductCommand.getTitle())
                .description(createProductCommand.getDescription())
                .unitType(createProductCommand.getUnitType())
                .pricePerUnit(createProductCommand.getPricePerUnit())
                .discountPercents(createProductCommand.getDiscountPercents())
                .quantity(createProductCommand.getQuantity())
                .category(category)
                .images(images).build();
        return productMapper.map(productRepository.save(product));
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, UpdateProductCommand updateProductCommand,
                                    Set<MultipartFile> files, Set<Long> imagesToDeleteId) {
        Set<Image> images = new HashSet<>();
        files.forEach(file -> {
            Image image = null;
            try {
                image = toImageEntity(file);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            images.add(image);
        });
        Product product = productRepository.getProductById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        Category category = categoryRepository.getCategoryById(updateProductCommand.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_FIND_FAILED_BY_ID));
        product.setTitle(updateProductCommand.getTitle());
        product.setDescription(updateProductCommand.getDescription());
        product.setUnitType(updateProductCommand.getUnitType());
        product.setPricePerUnit(updateProductCommand.getPricePerUnit());
        product.setDiscountPercents(updateProductCommand.getDiscountPercents());
        product.setQuantity(updateProductCommand.getQuantity());
        product.setCategory(category);
        product.setImages(images);
        imagesToDeleteId.forEach(imageId -> imageRepository.deleteById(imageId));
        return productMapper.map(product);
    }

    @Override
    @Transactional
    public ProductDTO deleteProduct(Long id) {
        Product product = productRepository.getProductById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        productRepository.delete(product);
        return productMapper.map(product);
    }

    @Override
    @Transactional
    public List<ProductDTO> getAllProducts() {
        List<Product> products = productRepository.findAll();
        return productMapper.mapProducts(products);
    }

    @Override
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.getProductById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        return productMapper.map(product);
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
