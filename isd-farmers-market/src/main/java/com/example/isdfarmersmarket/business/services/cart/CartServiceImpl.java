package com.example.isdfarmersmarket.business.services.cart;

import com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException;
import com.example.isdfarmersmarket.business.mapper.ItemInCartMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.dao.models.ItemInCart;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.ItemInCartRepository;
import com.example.isdfarmersmarket.dao.repositories.ProductRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.cart.AddItemInCartCommand;
import com.example.isdfarmersmarket.web.commands.cart.UpdateItemInCartCommand;
import com.example.isdfarmersmarket.web.dto.CartDTO;
import com.example.isdfarmersmarket.web.dto.ItemInCartDTO;
import jakarta.persistence.EntityExistsException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    ItemInCartRepository itemInCartRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    ItemInCartMapper itemInCartMapper;
    TotalPriceService totalPriceService;

    @Override
    @Transactional
    public ItemInCartDTO addToCart(AddItemInCartCommand addItemInCartCommand) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException(principal.getId(), User.class));

        Product product = productRepository
                .findById(addItemInCartCommand.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(addItemInCartCommand.getProductId(), Product.class));

        if (itemInCartRepository.existsByUserAndProduct(user, product)) {
            throw new EntityExistsException("Item already exists");
        }

        ItemInCart newItemInCart = itemInCartMapper.mapToEntity(addItemInCartCommand);
        newItemInCart.setUser(user);
        newItemInCart.setProduct(product);
        itemInCartRepository.save(newItemInCart);
        return itemInCartMapper.mapToDTO(newItemInCart);
    }

    @Override
    @Transactional
    public ItemInCartDTO removeFromCart(Long id) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        ItemInCart cartToRemove = itemInCartRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, ItemInCart.class));

        User authenticatedUser = userRepository
                .findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException(principal.getId(), User.class));
        if (!cartToRemove.getUser().equals(authenticatedUser)) {
            throw new AccessDeniedException("test");
        }
        itemInCartRepository.delete(cartToRemove);
        return itemInCartMapper.mapToDTO(cartToRemove);
    }

    @Override
    @Transactional
    public ItemInCartDTO updateCart(Long id, UpdateItemInCartCommand updateItemInCartCommand) {
        ItemInCart itemInCart = itemInCartRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, ItemInCart.class));

        itemInCart.setQuantity(updateItemInCartCommand.getQuantity());
        return itemInCartMapper.mapToDTO(itemInCartRepository.save(itemInCart));
    }

    @Override
    @Transactional(readOnly = true)
    public CartDTO getAllCartItems(DeliveryTypes deliveryTypes) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User authenticatedUser = userRepository
                .findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException(principal.getId(), User.class));

        List<ItemInCart> itemsInCart = itemInCartRepository.getAllByUser(authenticatedUser);
        List<ItemInCartDTO> itemInCartDTOS = itemInCartMapper.mapToDTOs(itemsInCart);

        BigDecimal totalPriceOfProducts = totalPriceService.getTotalPriceOfProducts(itemsInCart);
        BigDecimal totalPriceOfDelivery = totalPriceService.getTotalPriceOfDelivery(itemsInCart,deliveryTypes);

        CartDTO cartDTO = new CartDTO(itemInCartDTOS, totalPriceOfProducts, totalPriceOfDelivery, totalPriceOfProducts.add(totalPriceOfDelivery), deliveryTypes);
        return  cartDTO;
//        return itemInCartMapper.mapToDTOs(itemsInCart);
    }
}
