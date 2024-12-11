package com.example.isdfarmersmarket.web.controllers;
import com.example.isdfarmersmarket.business.services.order.OrderService;
import com.example.isdfarmersmarket.web.commands.order.CreateOrderCommand;
import com.example.isdfarmersmarket.web.commands.order.UpdateOrderCommand;
import com.example.isdfarmersmarket.web.dto.OrderDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public ResponseEntity<List<OrderDTO>> createOrder(@RequestBody @Valid CreateOrderCommand createOrderCommand) {
        return ResponseEntity.status(CREATED).body(orderService.createOrders(createOrderCommand));
    }

    @Operation(
            description = "This endpoint is used to update an order"
    )
    @PreAuthorize("hasRole('FARMER') or hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> farmerStatusChangeOrder(@PathVariable Long id, @RequestBody @Valid UpdateOrderCommand updateOrderCommand) {
        return ResponseEntity.status(OK).body(orderService.farmerStatusChangeOrder(id, updateOrderCommand));
    }

    @PreAuthorize("hasRole('CUSTOMER')")
    @PutMapping("/receive/{id}")
    public ResponseEntity<OrderDTO> customerReceivedOrder(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(orderService.customerReceivedOrder(id));
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
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('ADMIN')")
    @GetMapping()
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        return ResponseEntity.status(OK).body(orderService.getAllOrders());
    }
    @Operation(
            description = "This endpoint is used to get all current user's orders"
    )
    @PreAuthorize("hasRole('CUSTOMER')")
    @GetMapping("/management")
    public ResponseEntity<Page<OrderDTO>> getCurrentUserOrders(
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.status(OK).body(orderService.getCurrentUserOrders(status, pageable));
    }
    @Operation(
            description = "This endpoint is used to get all current user's orders"
    )
    @PreAuthorize("hasRole('FARMER')")
    @GetMapping("/farmer/management")
    public ResponseEntity<Page<OrderDTO>> getCurrentFarmerOrders(
            @RequestParam(required = false) String status,
            Pageable pageable) {
        return ResponseEntity.status(OK).body(orderService.getCurrentFarmerOrders(status, pageable));
    }

    @Operation(
            description = "This endpoint is used to get an order by id"
    )
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Long id) {
        return ResponseEntity.status(OK).body(orderService.getOrderById(id));
    }
}

