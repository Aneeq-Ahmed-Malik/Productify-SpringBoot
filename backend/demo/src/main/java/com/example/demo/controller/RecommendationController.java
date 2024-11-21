package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.service.RecommendationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/recommend")
public class RecommendationController {

    @Autowired
    private RecommendationService recommendationService;

    // Endpoint to get recommendations based on a list of product IDs
    @GetMapping("/get")
    public List<Product> getRecommendations(@RequestParam List<Long> productIds) throws IOException {
        return recommendationService.getRecommendations(productIds);
    }
}
