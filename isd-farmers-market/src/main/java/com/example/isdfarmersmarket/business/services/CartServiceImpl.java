package com.example.isdfarmersmarket.business.services;

import com.example.isdfarmersmarket.business.exception.custom_exceptions.EntityNotFoundException;
import com.example.isdfarmersmarket.business.mapper.ItemInCartMapper;
import com.example.isdfarmersmarket.business.security.JwtPrincipal;
import com.example.isdfarmersmarket.business.utils.SecurityUtils;
import com.example.isdfarmersmarket.dao.models.ItemInCart;
import com.example.isdfarmersmarket.dao.models.Product;
import com.example.isdfarmersmarket.dao.models.User;
import com.example.isdfarmersmarket.dao.repositories.CartRepository;
import com.example.isdfarmersmarket.dao.repositories.ProductRepository;
import com.example.isdfarmersmarket.dao.repositories.UserRepository;
import com.example.isdfarmersmarket.web.commands.ItemInCartCommand;
import com.example.isdfarmersmarket.web.dto.ItemInCartDTO;
import jakarta.persistence.EntityExistsException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    ItemInCartMapper itemInCartMapper;

    @Override
    @Transactional
    public void addToCard(ItemInCartCommand itemInCartCommand) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException(principal.getId(), User.class));
        Product product = productRepository
                .findById(itemInCartCommand.getProductId())
                .orElseThrow(() -> new EntityNotFoundException(itemInCartCommand.getProductId(), Product.class));

        if (cartRepository.existsByUserAndProduct(user, product)) {
            throw new EntityExistsException("Item already exists");
        }
        ItemInCart newItemInCart = itemInCartMapper.mapToEntity(itemInCartCommand);
        newItemInCart.setUser(user);
        newItemInCart.setProduct(product);
        cartRepository.save(newItemInCart);
    }

    @Override
    @Transactional
    public ItemInCartDTO removeFromCard(Long id) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        ItemInCart cartToRemove = cartRepository
                .findById(id)
                .orElseThrow(() -> new EntityNotFoundException(id, ItemInCart.class));

        User authenticatedUser = userRepository
                .findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException(principal.getId(), User.class));
        if (!cartToRemove.getUser().equals(authenticatedUser)) {
            throw new AccessDeniedException("test");
        }
        cartRepository.delete(cartToRemove);
        return itemInCartMapper.mapToDTO(cartToRemove);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ItemInCartDTO> getAllCartItems() {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User authenticatedUser = userRepository
                .findById(principal.getId())
                .orElseThrow(() -> new EntityNotFoundException(principal.getId(), User.class));

        List<ItemInCart> itemsInCart = cartRepository.getAllByUser(authenticatedUser);
        return itemInCartMapper.mapToDTOs(itemsInCart);
    }

}
