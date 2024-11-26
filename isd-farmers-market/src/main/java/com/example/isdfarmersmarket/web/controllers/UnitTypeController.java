package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.dao.enums.UnitType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/unit-types")
public class UnitTypeController {
    @GetMapping
    public ResponseEntity<List<String>> getUnitTypes() {
        List<String> unitTypes = Arrays.stream(UnitType.values())
                .map(Enum::name)
                .collect(Collectors.toList());

        return ResponseEntity.ok(unitTypes);
    }
}
