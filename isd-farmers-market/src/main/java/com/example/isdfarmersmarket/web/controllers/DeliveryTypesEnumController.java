package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.dao.enums.DeliveryTypes;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/delivery-types-enum")
public class DeliveryTypesEnumController {
    @GetMapping
    public ResponseEntity<List<String>> getDeliveryTypes() {
        List<String> deliveryTypes = Arrays.stream(DeliveryTypes.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return ResponseEntity.ok(deliveryTypes);
    }
}
