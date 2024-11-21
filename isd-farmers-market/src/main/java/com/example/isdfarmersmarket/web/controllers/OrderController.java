package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.order.OrderService;
import com.example.isdfarmersmarket.web.commands.order.CreateOrderCommand;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.HttpStatus.CREATED;

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
    public ResponseEntity<OrderDTO> createOrder(@RequestBody @Valid CreateOrderCommand createOrderCommand, Authentication authentication) {
        OrderDTO orderDTO = orderService.createOrder(createOrderCommand, authentication.getName());
        return ResponseEntity.status(CREATED).body(orderDTO);
    }

    @Operation(
            description = "This endpoint is used to update an order"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Long id, @RequestBody @Valid UpdateOrderCommand updateOrderCommand) {
        OrderDTO updatedOrderDTO = orderService.updateOrder(id, updateOrderCommand);
        return ResponseEntity.ok(updatedOrderDTO);
    }

    @Operation(
            description = "This endpoint is used to delete an order"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<OrderDTO> deleteOrder(@PathVariable Long id) {
        OrderDTO deletedOrderDTO = orderService.deleteOrder(id);
        return ResponseEntity.ok(deletedOrderDTO);
    }

//    @PreAuthorize("hasRole('CUSTOMER')")
//    @GetMapping("/{id}")
//    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
//        OrderDTO orderDTO = orderService.getOrderById(id);
//        return ResponseEntity.ok(orderDTO);
//    }
}
