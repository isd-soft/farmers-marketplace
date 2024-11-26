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
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;


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
    public ProductDTO createProduct(CreateProductCommand createProductCommand) {
        Set<Image> images = new HashSet<>();
        if (createProductCommand.getImagesBase64() != null && !createProductCommand.getImagesBase64().isEmpty()) {
            createProductCommand.getImagesBase64().forEach(file -> {
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
        Product savedProduct = productRepository.save(product);
        images.forEach(image -> {image.setProduct(savedProduct);});
        return productMapper.map(savedProduct);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, UpdateProductCommand updateProductCommand,
                                    Set<MultipartFile> files, Set<Long> imagesToDeleteId) {
        Set<Image> images = new HashSet<>();
        if (updateProductCommand.getImagesBase64() != null && !updateProductCommand.getImagesBase64().isEmpty()) {
            updateProductCommand.getImagesBase64().forEach(file -> {
                Image image = null;
                try {
                    image = toImageEntity(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                images.add(image);
            });
        }
        Product product = productRepository.findById(id)
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
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        productRepository.delete(product);
        return productMapper.map(product);
    }
    @Override
    @Transactional
    public List<ProductDTO> getAllProducts(Long category,String search) {
        if (category == null && (search == null || search.isBlank())) {
            return productMapper.mapProducts(productRepository.findAll());
        }
        List<Product> filteredProducts = productRepository.findAll().stream()
                .filter(product -> (category == null || product.getCategory().getId().equals(category)))
                .filter(product -> (search == null || search.isBlank()
                        || product.getTitle().toLowerCase().contains(search.toLowerCase())
                        || product.getDescription().toLowerCase().contains(search.toLowerCase())))
                .collect(Collectors.toList());

        return productMapper.mapProducts(filteredProducts);
    }

    @Override
    @Transactional
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        ProductDTO productDTO = productMapper.map(product);
        return productDTO;
    }
    @Override
    @Transactional(readOnly = true)
    public PageResponseDTO<ProductReviewDTO> getProductReviews(Long productId, int page, int pageSize) {
        Product product = productRepository
                .findById(productId)
                .orElseThrow(()->new EntityNotFoundException("Product not found"));

        var reviewsPage = productReviewRepository
                .findAllByProductOrderByCreatedDateDesc(product, PageRequest.of(page,pageSize));
        var totalReviews = reviewsPage.getTotalElements();
        var content = reviewsPage
                .getContent()
                .stream()
                .map(reviewMapper::mapWithoutProductDetails)
                .toList();

        return new PageResponseDTO<>(content,totalReviews,page,pageSize);
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
                .findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        User user = userRepository.findById(principal.getId()).orElseThrow();
        ProductPageDTO productPageDTO = productMapper.mapToProductPage(product);
        if(user.getWishlist().contains(product)) productPageDTO.setIsInWishlist(true);
        return productPageDTO;
    }

    private Image toImageEntity(String base64Image) throws IOException {
        if (base64Image.contains(",")) {
            base64Image = base64Image.split(",")[1];
        }

        byte[] decodedBytes = Base64.getDecoder().decode(base64Image);
        Image image = new Image();
        image.setBytes(decodedBytes);
        return image;
    }
}
