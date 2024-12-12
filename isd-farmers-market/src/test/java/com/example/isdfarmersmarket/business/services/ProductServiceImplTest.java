package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException;
import com.example.isdfarmersmarket.business.mapper.ProductMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.dao.models.Category;
import com.example.isdfarmersmarket.dao.models.DeliveryTypeFarmer;
import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.*;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductServiceImplTest {

    private static final Long USER_ID = 1312L;
    private static final Long PRODUCT_ID = 2122L;
    private static final Long CATEGORY_ID = 12334L;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ImageRepository imageRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    OrderRepository orderRepository;

    @Mock
    ItemInCartRepository itemInCartRepository;

    @Mock
    PlannedOrderRepository plannedOrderRepository;

    @InjectMocks
    private ProductServiceImpl productService;

    private Category category;
    private Product product;
    private User user;
    private CreateProductCommand createProductCommand;
    private UpdateProductCommand updateProductCommand;

    @BeforeEach
    void setUp() {
        JwtPrincipal mockPrincipal = new JwtPrincipal(USER_ID, "test@example.com", List.of(ERole.CUSTOMER, ERole.FARMER));
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockPrincipal);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);

        category = new Category();
        category.setId(CATEGORY_ID);

        user = new User();
        user.setId(USER_ID);
        user.setDeliveryTypes(Set.of(
                new DeliveryTypeFarmer(1L, new BigDecimal(100), DeliveryTypes.COURIER, user),
                new DeliveryTypeFarmer(2L, new BigDecimal(100), DeliveryTypes.PICKUP, user),
                new DeliveryTypeFarmer(3L, new BigDecimal(100), DeliveryTypes.POSTAL, user)
        ));

        product = new Product();
        product.setId(PRODUCT_ID);

        createProductCommand = new CreateProductCommand();
        createProductCommand.setTitle("Test Product");
        createProductCommand.setCategoryId(CATEGORY_ID);
        createProductCommand.setPricePerUnit(BigDecimal.valueOf(100));
        createProductCommand.setQuantity(10);

        when(userRepository.findById(USER_ID)).thenReturn(Optional.of(user));
        when(categoryRepository.getCategoryById(CATEGORY_ID)).thenReturn(Optional.of(category));

        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(PRODUCT_ID);

        when(productMapper.map(any())).thenReturn(productDTO);
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.of(product));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        updateProductCommand = new UpdateProductCommand();
        updateProductCommand.setPricePerUnit(BigDecimal.valueOf(100));
        updateProductCommand.setQuantity(10);

    }

    @Test
    void createProduct_whenInvoked_createsProduct() {

        ProductDTO result = productService.createProduct(createProductCommand);

        assertNotNull(result, "ProductDTO cannot be null");
        assertEquals(product.getId(), result.getId(), "Product ID must match");

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product savedProduct = productCaptor.getValue();

        assertEquals("Test Product", savedProduct.getTitle(), "Title must match");
        assertEquals(category, savedProduct.getCategory(), "Category must match");
        assertEquals(user, savedProduct.getFarmer(), "Farmer must match");
    }

    @Test
    void createProduct_whenUserLacksDeliveryTypes_throwsAccessDeniedException() {
        user.setDeliveryTypes(Set.of(
                new DeliveryTypeFarmer(1L, new BigDecimal(100), DeliveryTypes.COURIER, user)
        ));

        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
           productService.createProduct(createProductCommand);
        });
        assertEquals("You must add all available delivery types before posting any products", exception.getMessage());
    }

    @Test
    void createProduct_whenCategoryNotFound_throwsEntityNotFoundException() {
        when(categoryRepository.getCategoryById(CATEGORY_ID)).thenReturn(Optional.empty());

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.createProduct(createProductCommand);
        });

        assertEquals(Category.class, exception.getAClass());
        assertEquals(CATEGORY_ID, exception.getId());
    }

    @Test
    void createProduct_whenUserNotFound_throwsEntityNotFoundException() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.createProduct(createProductCommand);
        });

        assertEquals(User.class, exception.getAClass());
        assertEquals(USER_ID, exception.getId());
    }

    @Test
    void updateProduct_whenInvoked_updatesProduct() {
        product.setFarmer(user);

        ProductDTO result = productService.updateProduct(PRODUCT_ID,updateProductCommand);

        assertNotNull(result, "ProductDTO cannot be null");
        assertEquals(product.getId(), result.getId(), "Product ID must match");

        ArgumentCaptor<Product> productCaptor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(productCaptor.capture());
        Product savedProduct = productCaptor.getValue();

        assertEquals(10, savedProduct.getQuantity(), "Quantity must match");
    }
    @Test
    void updateProduct_whenUserIsNotCreator_throwsAccessDeniedException() {
        User creator = new User();
        creator.setId(2L);
        product.setFarmer(creator);
        updateProductCommand = new UpdateProductCommand();
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            productService.updateProduct(PRODUCT_ID, updateProductCommand);
        });
        assertEquals("You are not the owner of this product", exception.getMessage());
    }
    @Test
    void updateProduct_whenUserNotFound_throwsEntityNotFoundException() {
        when(userRepository.findById(USER_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProduct(PRODUCT_ID, updateProductCommand);
        });
        assertEquals(User.class, exception.getAClass());
        assertEquals(USER_ID, exception.getId());
    }
    @Test
    void updateProduct_whenProductNotFound_throwsEntityNotFoundException() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.updateProduct(PRODUCT_ID, updateProductCommand);
        });
        assertEquals(Product.class, exception.getAClass());
        assertEquals(PRODUCT_ID, exception.getId());
    }
    @Test
    void deleteProduct_whenInvoked_deletesProduct() {
        product.setFarmer(user);
        when(orderRepository.countOrdersByProductId(PRODUCT_ID)).thenReturn(0L);
        productService.deleteProduct(PRODUCT_ID);
        verify(productRepository).delete(product);
    }
    @Test
    void deleteProduct_whenUserIsNotCreator_throwsAccessDeniedException() {
        User creator = new User();
        creator.setId(2L);
        product.setFarmer(creator);
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            productService.deleteProduct(PRODUCT_ID);
        });
        assertEquals("You are not the owner of this product", exception.getMessage());
    }

    @Test
    void deleteProduct_whenProductHasOrders_throwsAccessDeniedException() {
        product.setFarmer(user);
        when(orderRepository.countOrdersByProductId(PRODUCT_ID)).thenReturn(1L);
        AccessDeniedException exception = assertThrows(AccessDeniedException.class, () -> {
            productService.deleteProduct(PRODUCT_ID);
        });
        assertEquals("You can not delete product with orders", exception.getMessage());
    }

    @Test
    void deleteProduct_whenProductNotFound_throwsEntityNotFoundException() {
        when(productRepository.findById(PRODUCT_ID)).thenReturn(Optional.empty());
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            productService.deleteProduct(PRODUCT_ID);
        });
        assertEquals(Product.class, exception.getAClass());
        assertEquals(PRODUCT_ID, exception.getId());
    }

    @Test
    void getProductById_whenInvoked_getsProduct() {
        ProductDTO result = productService.getProductById(PRODUCT_ID);

        assertNotNull(result, "ProductDTO cannot be null");
        assertEquals(product.getId(), result.getId(), "Product ID must match");

    }
}