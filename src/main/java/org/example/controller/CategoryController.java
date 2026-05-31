package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.model.VehicleCategoryConfig;
import org.example.services.VehicleCategoryConfigService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final VehicleCategoryConfigService configService;

    @GetMapping
    public List<VehicleCategoryConfig> list() {
        return configService.findAllCategories();
    }

    @GetMapping("/{category}")
    public VehicleCategoryConfig get(@PathVariable String category) {
        return configService.getByCategory(category);
    }
}