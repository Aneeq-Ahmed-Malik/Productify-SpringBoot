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
    @CrossOrigin(origins = {"https://productify.live", "https://www.productify.live"})
    @GetMapping("/sentiment")
    public String getRecommendations(@RequestParam String link) {
        return reviewAnalysisService.getSentiment(link);
    }

}
