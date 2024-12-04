package com.example.isdfarmersmarket.web.controllers;

import com.example.isdfarmersmarket.business.services.PerformanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/performance")
public class PerformanceController {

    private final PerformanceService performanceService;

}