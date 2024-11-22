package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.scraping.Scraper;
import com.example.demo.scraping.ScraperFactory;
import com.example.demo.service.CSVImportService;
import com.example.demo.service.ProductRetrevalService;
import com.example.demo.service.RecommendationService;
import com.example.demo.service.AdServices;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import com.example.demo.service.AdServices;

@RestController
@RequestMapping("/api/products")
public class ProductifyController {

    @Autowired
    private CSVImportService csvImportService;
    @Autowired
    private ProductRetrevalService productRetrievalService;
    @Autowired
    private RecommendationService recommendationService;
    @Autowired
    private AdServices adServices;



    @GetMapping("/import/categories")
    public String importCategories() {
        csvImportService.importCategories();
        return "Categories imported successfully!";
    }

    @GetMapping("/import/websites")
    public String importWebsites() {
        csvImportService.importWebsites();
        return "Websites imported successfully!";
    }

    @GetMapping("/import/products")
    public String importProducts(@RequestParam String webName) {
        csvImportService.importProducts(webName);
        return "Products imported successfully!";
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/allproducts")
    public List<Product> getAllProducts() {
        return productRetrievalService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productRetrievalService.getProductById(id);
    }

    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/{categoryName}/{websiteName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName, @PathVariable String websiteName) {
        return productRetrievalService.getProductsByWebsiteandCategory(categoryName, websiteName);
    }


    @CrossOrigin(origins = "http://localhost:4200")
    @GetMapping("/category/{categoryName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName) {
        return productRetrievalService.getProductsByCategory(categoryName);
    }

    @GetMapping("/website/{websiteName}")
    public List<Product> getProductsByWebsite(@PathVariable String websiteName) {
        return productRetrievalService.getProductsByWebsite(websiteName);
    }

    @GetMapping("/search")
    public List<Product> searchProductsByTitle(@RequestParam String query,  @RequestParam int limit) {
        return recommendationService.searchProducts(query, limit);
    }

    @GetMapping("/price-range")
    public List<Product> getProductsByPriceRange(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return productRetrievalService.getProductsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("/scrape")
    public String scrape(@RequestParam String platform, @RequestParam String categoryFileName) {
        try {
            Scraper scraper = ScraperFactory.getInstance().getScraper(platform);

            // Trigger asynchronous scraping
            initiateScrapingAsync(scraper, categoryFileName);
            return "Scraping initiated successfully for platform: " + platform;
        } catch (IllegalArgumentException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }


    @Async // Run this process asynchronously to avoid blocking
    public void initiateScrapingAsync(Scraper scraper, String categoryCSVPath) {
        scraper.initiateScraping(categoryCSVPath);
    }

   
    @PostMapping("/postAd")
    public String postAd(
            @RequestParam String title,
            @RequestParam String desc,
            @RequestParam Long userId,
            @RequestParam String location,
            @RequestParam String phoneNo,
            @RequestParam Long price,
            @RequestParam MultipartFile image1,
            @RequestParam(required = false) MultipartFile image2,
            @RequestParam(required = false) MultipartFile image3,
            @RequestParam(required = false) MultipartFile image4) {
        try {
            // Save ad details in the database
            Long adId = adServices.postAd(phoneNo, title, desc, userId, location, price,image1, image2, image3, image4);
            
            return "Ad posted successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to post the ad: " + e.getMessage();
        }
    }


}
