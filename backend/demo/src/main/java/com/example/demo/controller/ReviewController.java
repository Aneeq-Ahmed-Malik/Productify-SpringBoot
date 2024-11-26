package com.example.demo.controller;

import com.example.demo.service.ReviewAnalysisService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/review")
public class ReviewController {

    @Autowired
    private ReviewAnalysisService reviewAnalysisService;

    @GetMapping("/analyze")
    public String getRecommendations() {
        return reviewAnalysisService.analyzeReviews();
    }
    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/sentiment")
    public String getRecommendations(@RequestParam String link) {
        return reviewAnalysisService.getSentiment(link);
    }

}
