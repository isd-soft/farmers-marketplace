package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.cart.CartService;
import com.example.isdfarmersmarket.web.commands.cart.AddItemInCartCommand;
import com.example.isdfarmersmarket.web.commands.cart.UpdateItemInCartCommand;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.ItemInCartDTO;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartController {
    CartService cartService;

    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<ItemInCartDTO> addToCard(@RequestBody @Valid AddItemInCartCommand itemInCart) {
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

    @Operation(
            description = "This endpoint is used to update an item in cart"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<ItemInCartDTO> updateOrder(@PathVariable Long id, @RequestBody @Valid UpdateItemInCartCommand updateItemInCartCommand) {
        return ResponseEntity.status(OK).body(cartService.updateCart(id, updateItemInCartCommand));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping
    public ResponseEntity<List<ItemInCartDTO>> getAllFromCart() {
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(cartService.getAllCartItems());
    }
}
