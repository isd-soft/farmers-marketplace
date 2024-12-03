package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.CartService;
import com.example.isdfarmersmarket.web.commands.ItemInCartCommand;
import com.example.isdfarmersmarket.web.dto.ItemInCartDTO;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public Map<String, String> addToCard(@RequestBody @Valid ItemInCartCommand itemInCart) {
        cartService.addToCard(itemInCart);
        return Map.of("message", "Item added to your cart.");
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    public Map<String, String> removeFromCart(@PathVariable Long id) {
        cartService.removeFromCard(id);
        return Map.of("message", "Item removed from your cart.");
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<ItemInCartDTO>> getAllFromCart() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.getAllCartItems());
    }

}
