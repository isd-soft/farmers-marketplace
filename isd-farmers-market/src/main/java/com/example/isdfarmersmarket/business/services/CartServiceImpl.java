package com.example.isdfarmersmarket.business.services;

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
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    ProductRepository productRepository;
    UserRepository userRepository;
    ItemInCartMapper itemInCartMapper;

    @Override
    public void addToCard(ItemInCartCommand itemInCartCommand) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        User user = userRepository.findById(principal.getId())
                .orElseThrow(EntityNotFoundException::new);
        Product product = productRepository
                .findById(itemInCartCommand.getProductId())
                .orElseThrow(EntityNotFoundException::new);

        if(cartRepository.existsByUserAndProduct(user, product)) {
            throw new EntityExistsException("Item already exists");
        }
        ItemInCart newItemInCart = itemInCartMapper.map(itemInCartCommand);
        newItemInCart.setUser(user);
        newItemInCart.setProduct(product);
        cartRepository.save(newItemInCart);
    }

    @Override
    public void removeFromCard(Long id) {
        JwtPrincipal principal = SecurityUtils.getPrincipal();
        ItemInCart cartToRemove = cartRepository
                .findById(id)
                .orElseThrow(EntityNotFoundException::new);
        User authenticatedUser = userRepository
                .findById(principal.getId())
                .orElseThrow(EntityNotFoundException::new);
        if (!cartToRemove.getUser().equals(authenticatedUser)){
            throw new AccessDeniedException("test");
        }
        cartRepository.delete(cartToRemove);
    }
}
