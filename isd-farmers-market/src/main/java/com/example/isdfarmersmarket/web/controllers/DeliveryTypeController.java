package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.DeliveryTypeService;
import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import com.example.isdfarmersmarket.web.commands.CreateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.commands.UpdateDeliveryTypeCommand;
import com.example.isdfarmersmarket.web.dto.DeliveryTypeDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@RestController
@RequestMapping("/deliverytypes")
@RequiredArgsConstructor
public class DeliveryTypeController {
    private final DeliveryTypeService deliveryTypeService;

    @PostMapping()
    public ResponseEntity<DeliveryTypeDTO> createDeliveryType(
            @RequestBody @Valid CreateDeliveryTypeCommand createDeliveryTypeCommand) {
        return ResponseEntity.status(CREATED)
                .body(deliveryTypeService.createDeliveryType(createDeliveryTypeCommand));
    }

    @PutMapping()
    public ResponseEntity<DeliveryTypeDTO> updateDeliveryType(
            @RequestBody @Valid UpdateDeliveryTypeCommand updateDeliveryTypeCommand) {
        return ResponseEntity.status(OK)
                .body(deliveryTypeService.updateDeliveryType(updateDeliveryTypeCommand));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeliveryTypeDTO> deleteDeliveryType(
            @PathVariable Long id) {
        return ResponseEntity.status(OK)
                .body(deliveryTypeService.deleteDeliveryType(id));
    }

    @GetMapping
    public ResponseEntity<List<DeliveryTypeDTO>> getDeliveryTypes() {
        return ResponseEntity.status(OK)
                .body(deliveryTypeService.getAllDeliveryType());
    }
    @PreAuthorize("hasRole('FARMER')")
    @GetMapping("/current-farmer")
    public ResponseEntity<List<String>> getCurrentFarmerDeliveryTypes() {
        return ResponseEntity.status(OK)
                .body(deliveryTypeService.getAllCurrentUserDeliveryType());
    }
    @GetMapping("/price")
    public ResponseEntity<BigDecimal> getDeliveryTypePrice(@RequestParam String farmerEmail, @RequestParam DeliveryTypes deliveryType) {
        return ResponseEntity.status(OK)
                .body(deliveryTypeService.getDeliveryTypePrice(farmerEmail, deliveryType));
    }
}
