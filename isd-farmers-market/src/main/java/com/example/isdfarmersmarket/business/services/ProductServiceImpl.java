package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.mapper.ProductMapper;
import com.example.isdfarmersmarket.business.mapper.ReviewMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.dao.models.Category;
import com.example.isdfarmersmarket.dao.models.Image;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;
    private final ImageRepository imageRepository;
    private final ProductReviewRepository productReviewRepository;
    private final ReviewMapper reviewMapper;
    private static final String PRODUCT_FIND_FAILED_BY_ID = "Product with the specified id not found";
    private static final String CATEGORY_FIND_FAILED_BY_ID = "Category with the specified id not found";
    private final UserRepository userRepository;

    @Override
    @Transactional
    public ProductDTO createProduct(CreateProductCommand createProductCommand, Set<MultipartFile> files) {
        Set<Image> images = new HashSet<>();
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                Image image = null;
                try {
                    image = toImageEntity(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                images.add(image);
            });
        }
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
        ProductDTO savedProduct = productMapper.map(productRepository.save(product));
        return savedProduct;
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, UpdateProductCommand updateProductCommand,
                                    Set<MultipartFile> files, Set<Long> imagesToDeleteId) {
        Set<Image> images = new HashSet<>();
        if (files != null && !files.isEmpty()) {
            files.forEach(file -> {
                Image image = null;
                try {
                    image = toImageEntity(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                images.add(image);
            });
        }
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
        if (imagesToDeleteId != null && !imagesToDeleteId.isEmpty()) {
            imagesToDeleteId.forEach(imageId -> imageRepository.deleteById(imageId));
        }
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
    @Transactional
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

    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductReviewDTO> getProductReviews(Long productId, int page, int pageSize) {
        Product product = productRepository
                .getProductById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Product not found"));

        var reviewsPage = productReviewRepository
                .findAllByProductOrderByCreatedDateDesc(product, PageRequest.of(page, pageSize));
        var totalReviews = reviewsPage.getTotalElements();
        var content = reviewsPage
                .getContent()
                .stream()
                .map(reviewMapper::mapWithoutProductDetails)
                .toList();

        return new PageResponseDTO<>(content, totalReviews, page, pageSize);
    }

    @Override
    @Transactional
    public void updateProductReview(Product product) {
        ProductReviewStatsDTO productReviewStatsDTO = productReviewRepository
                .findReviewStatsByProduct(product);

        product.setRating(productReviewStatsDTO
                .getAverageRating()
                .floatValue());
        product.setReviewCount(productReviewStatsDTO
                .getReviewCount()
                .intValue());

        productRepository.save(product);
    }

    @Override
    @Transactional
    public ProductPageDTO getProductPageById(Long productId, @AuthenticationPrincipal JwtPrincipal principal) {
        var product = productRepository
                .getProductById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        User user = userRepository.findById(principal.getId()).orElseThrow();
        ProductPageDTO productPageDTO = productMapper.mapToProductPage(product);
        if (user.getWishlist().contains(product)) productPageDTO.setIsInWishlist(true);
        return productPageDTO;
    }
}
