package com.example.isdfarmersmarket.business.services;
import com.example.isdfarmersmarket.business.mapper.ProductMapper;
import com.example.isdfarmersmarket.business.mapper.ReviewMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.interfaces.ProductService;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.*;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.dao.specifications.ProductSpecification;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.io.IOException;
import java.util.*;


@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ProductServiceImpl implements ProductService {
    ProductRepository productRepository;
    ProductMapper productMapper;
    CategoryRepository categoryRepository;
    ImageRepository imageRepository;
    ProductReviewRepository productReviewRepository;
    ReviewMapper reviewMapper;
    static String PRODUCT_FIND_FAILED_BY_ID = "Product with the specified id not found";
    static String CATEGORY_FIND_FAILED_BY_ID = "Category with the specified id not found";
    UserRepository userRepository;
    OrderRepository orderRepository;
    ItemInCartRepository itemInCartRepository;
    private final PlannedOrderRepository plannedOrderRepository;

    @Override
    @Transactional
    public ProductDTO createProduct(CreateProductCommand createProductCommand) {
        JwtPrincipal principal = (JwtPrincipal) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User creator = userRepository.findById(principal.getId()).orElse(null);
        List<Image> images = new ArrayList<>();
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
        imageRepository.saveAll(images);
        Category category = categoryRepository.getCategoryById(createProductCommand.getCategoryId())
                .orElseThrow(() -> new EntityNotFoundException(CATEGORY_FIND_FAILED_BY_ID));
        Product product = Product.builder()
                .title(createProductCommand.getTitle())
                .description(createProductCommand.getDescription())
                .unitType(createProductCommand.getUnitType())
                .pricePerUnit(createProductCommand.getPricePerUnit())
                .quantity(createProductCommand.getQuantity())
                .category(category)
                .farmer(creator)
                .visible(true)
                .images(new HashSet<>(images)).build();
        productRepository.save(product);
        if (creator != null) {
            creator.getMyProducts().add(product);
            userRepository.save(creator);
        }
        images.forEach(image -> {
            image.setProduct(product);
        });
        return productMapper.map(product);
    }

    @Override
    @Transactional
    public ProductDTO updateProduct(Long id, UpdateProductCommand updateProductCommand) {
        if (!isProductOwner(id)) {
            throw new AccessDeniedException("You are not the owner of this product");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        product.setDescription(updateProductCommand.getDescription());
        product.setUnitType(updateProductCommand.getUnitType());
        product.setPricePerUnit(updateProductCommand.getPricePerUnit());
        product.setQuantity(updateProductCommand.getQuantity());
        product.getImages().clear();

        List<Image> images = new ArrayList<>();
        if (updateProductCommand.getImagesBase64() != null && !updateProductCommand.getImagesBase64().isEmpty()) {
            updateProductCommand.getImagesBase64().forEach(file -> {
                Image image = null;
                try {
                    image = toImageEntity(file);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                image.setProduct(product);
                images.add(image);
            });
        }
        imageRepository.saveAll(images);
        images.forEach(image -> {
            product.getImages().add(image);
        });
        return productMapper.map(product);
    }

    @Override
    @Transactional
    public ProductDTO setDiscountProduct(Long id, int discount) {
        if (!isProductOwner(id)) {
            throw new AccessDeniedException("You are not the owner of this product");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        product.setDiscountPercents(discount);
        productRepository.save(product);
        return productMapper.map(product);
    }
    @Transactional
    @Override
    public ProductDTO changeVisible(Long id, boolean visible) {
        if (!isProductOwner(id) && !isAppAdmin()) {
            throw new AccessDeniedException("You are not the owner of this product");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        product.setVisible(visible);
        itemInCartRepository.deleteAllByProduct(product);
        plannedOrderRepository.deleteAllByProduct(product);
        productRepository.save(product);
        return productMapper.map(product);
    }

    @Override
    @Transactional
    public ProductDTO deleteProduct(Long id) {
        if (!isProductOwner(id) && !isAppAdmin()) {
            throw new AccessDeniedException("You are not the owner of this product");
        }
        if(orderRepository.countOrdersByProductId(id) > 0) {
            throw new AccessDeniedException("You can not delete product with orders");
        }
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        itemInCartRepository.deleteAllByProduct(product);
        plannedOrderRepository.deleteAllByProduct(product);
        productRepository.delete(product);
        return productMapper.map(product);
    }

    @Override
    @Transactional
    public Page<CompactProductDTO> getAllProducts(Long category, String search, Pageable pageable) {
        Specification<Product> filters = Specification
                .where(StringUtils.isBlank(search) ? null : ProductSpecification.titleOrDescLike(search))
                .and((category == null || category == 0L) ? null : ProductSpecification.categoryIs(category))
                .and(ProductSpecification.isVisible());
        Page<Product> products = productRepository.findAll(filters, pageable);
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        Set<Product> wishlist = new HashSet<>();
        if (principal != null) {
            User user = userRepository.findById(principal.getId()).orElse(null);
            if(user!=null) {
                wishlist = user.getWishlist();
            }
        }
        return productMapper.mapToCompactProductsDTO(products, wishlist);
    }
    @Override
    @Transactional
    public Page<CompactProductDTO> getCurrentUserProducts(Pageable pageable) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        Long creator = principal.getId();
        Specification<Product> filters = Specification.
                where((creator == null || creator == 0L) ? null : ProductSpecification.creatorIs(creator));
        Page<CompactProductDTO> productDTOS = productMapper.mapToCompactProductsDTO(productRepository.findAll(filters, pageable));
        productDTOS.forEach(product -> {product.setOrders(orderRepository.countOrdersByProductId(product.getId()));});
        return productDTOS;
    }

    @Override
    @Transactional
    public ProductDTO getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        ProductDTO productDTO = productMapper.map(product);
        return productDTO;
    }

    @Transactional
    @Override
    public Page<CompactProductDTO> getFarmersProducts(Long farmerId, Pageable pageable) {
        User farmer = userRepository.findById(farmerId)
                .orElseThrow(EntityNotFoundException::new);
        Page<Product> products = productRepository.findProductsByFarmer(farmer, pageable);
        return productMapper.mapToCompactProductsDTO(products);
    }

    @Override
    @Transactional
    public ProductPageDTO getProductPageById(Long productId) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        var product = productRepository
                .findById(productId)
                .orElseThrow(() -> new EntityNotFoundException(PRODUCT_FIND_FAILED_BY_ID));
        if(!product.isVisible() && !isProductOwner(product.getId())) {
            throw new AccessDeniedException("This product is unavaliable");
        }
        ProductPageDTO productPageDTO = productMapper.mapToProductPage(product);
        if (principal != null) {
            User user = userRepository.findById(principal.getId()).orElseThrow();
            if (user.getWishlist().contains(product)) productPageDTO.setIsInWishlist(true);
        }
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

    private boolean isProductOwner(Long productId) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User currentUser = userRepository.findById(principal.getId())
                .orElseThrow(() -> new com.example.isdfarmersmarket.business
                        .exception.custom_exceptions.EntityNotFoundException(principal.getId(), User.class));
            Product product = productRepository.findById(productId)
                    .orElseThrow(() -> new RuntimeException("Product not found"));
            return product.getFarmer().equals(currentUser);
        }
    private boolean isAppAdmin() {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User currentUser = userRepository.findById(principal.getId())
                .orElseThrow(() -> new com.example.isdfarmersmarket.business
                        .exception.custom_exceptions.EntityNotFoundException(principal.getId(), User.class));
        Set<Role> roles = currentUser.getRoles();
        return roles.stream()
                .anyMatch(role -> role.getRole() == ERole.ADMIN);
    }
}
