package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.order.OrderService;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @Operation(
            responses = @ApiResponse(
                    description = "Created",
                    responseCode = "201"
            ),
            description = "This endpoint is used to create an order"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @PostMapping
    public ResponseEntity<List<OrderDTO>> createOrder() {
        return ResponseEntity.status(CREATED).body(orderService.createOrders());
    }

    @Operation(
            description = "This endpoint is used to update an order"
    )
    @PreAuthorize("hasRole('FARMER')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody @Valid UpdateOrderCommand updateOrderCommand) {
        return ResponseEntity.status(OK).body(orderService.updateOrder(id, updateOrderCommand));
    }

    @Operation(
            description = "This endpoint is used to delete an order"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDTO> deleteOrder(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(orderService.deleteOrder(id));
    }

    @Operation(
            description = "This endpoint is used to get all orders"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.status(OK).body(orderService.getAllOrders());
    }

    @Operation(
            description = "This endpoint is used to get an order by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(orderService.getOrderById(id));
    }
}

