package com.example.isdfarmersmarket.web.controllers;
import com.example.isdfarmersmarket.business.configurations.serverinfo.services.RequestTracker;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.services.JwtServiceImpl;
import com.example.isdfarmersmarket.business.services.interfaces.ProductService;
import com.example.isdfarmersmarket.dao.enums.ERole;
import com.example.isdfarmersmarket.web.commands.CreateProductCommand;
import com.example.isdfarmersmarket.web.commands.UpdateProductCommand;
import com.example.isdfarmersmarket.web.dto.CompactProductDTO;
import com.example.isdfarmersmarket.web.dto.ProductDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.List;
@AutoConfigureMockMvc
@WebMvcTest(ProductController.class)
@ExtendWith(SpringExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class ProductControllerTest {
    private static final Long USER_ID = 1312L;
    private static final Long PRODUCT_ID = 2122L;
    private static final Long CATEGORY_ID = 12334L;

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private RequestTracker requestTracker;
    @MockBean
    private JwtServiceImpl jwtServiceImpl;
    @MockBean
    private ProductService productService;
    private CreateProductCommand createProductCommand;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {
        createProductCommand = new CreateProductCommand();
        createProductCommand.setTitle("Test Product");
        createProductCommand.setCategoryId(CATEGORY_ID);
        createProductCommand.setPricePerUnit(BigDecimal.valueOf(100));
        createProductCommand.setQuantity(10);
        productDTO = new ProductDTO();
        productDTO.setId(PRODUCT_ID);

        when(productService.createProduct(createProductCommand)).thenReturn(productDTO);
    }
    @Test
    @WithMockUser(roles = {"FARMER"})
    void createProduct_whenValidRequest_createsProduct() throws Exception {
        JwtPrincipal mockPrincipal = new JwtPrincipal(USER_ID, "farmer@example.com", List.of(ERole.FARMER));
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockPrincipal);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        mockMvc.perform(post("/product/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createProductCommand)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(PRODUCT_ID));

        verify(productService).createProduct(any(CreateProductCommand.class));
    }
    @Test
    void createProduct_whenUserUnauthorised_statusUnauthorised() throws Exception {
        mockMvc.perform(post("/product/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createProductCommand)))
                .andExpect(status().isUnauthorized());
    }
    @Test
    void createProduct_whenUserIsCustomer_statusForbidden() throws Exception {
        JwtPrincipal mockPrincipal = new JwtPrincipal(USER_ID, "customer@example.com", List.of(ERole.CUSTOMER));
        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(mockPrincipal);
        SecurityContext securityContext = mock(SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        mockMvc.perform(post("/product/create")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(createProductCommand)))
                .andExpect(status().isForbidden());
    }
}