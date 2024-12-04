package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.interfaces.CartService;
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
    public ResponseEntity<ItemInCartDTO> addToCard(@RequestBody @Valid ItemInCartCommand itemInCart) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(cartService.addToCart(itemInCart));
    }
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<ItemInCartDTO> removeFromCart(@PathVariable Long id) {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.removeFromCart(id));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<ItemInCartDTO>> getAllFromCart() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.getAllCartItems());
    }

}
