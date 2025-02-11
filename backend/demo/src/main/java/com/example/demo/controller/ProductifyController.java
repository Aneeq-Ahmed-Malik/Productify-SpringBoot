package com.example.demo.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.model.Product;
import com.example.demo.scraping.Scraper;
import com.example.demo.scraping.ScraperFactory;
import com.example.demo.service.CSVImportService;
import com.example.demo.service.FuzzyMatchingService;
import com.example.demo.service.NotificationManager;
import com.example.demo.service.ProductRetrevalService;

@RestController
@RequestMapping("/api/products")
public class ProductifyController {

    @Autowired
    private CSVImportService csvImportService;
    @Autowired
    private ProductRetrevalService productRetrievalService;
    @Autowired
    private FuzzyMatchingService searchService;
    @Autowired
    private NotificationManager notificationService;
   


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

    @GetMapping("/import/reviews")
    public String importReviews(@RequestParam String webName) {
        csvImportService.importReviews(webName);
        return "Reviews imported successfully!";
    }

    @CrossOrigin(origins = {"https://productify.live", "https://www.productify.live"})
    @GetMapping("/allproducts")
    public List<Product> getAllProducts() {
        return productRetrievalService.getAllProducts();
    }

    @GetMapping("/{id}")
    public Optional<Product> getProductById(@PathVariable Long id) {
        return productRetrievalService.getProductById(id);
    }

    @CrossOrigin(origins = {"https://productify.live", "https://www.productify.live"})
    @GetMapping("/{categoryName}/{websiteName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName, @PathVariable String websiteName) {
        return productRetrievalService.getProductsByWebsiteandCategory(categoryName, websiteName);
    }


    @CrossOrigin(origins = {"https://productify.live", "https://www.productify.live"})
    @GetMapping("/category/{categoryName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName) {
        return productRetrievalService.getProductsByCategory(categoryName);
    }

  
    @CrossOrigin(origins = {"https://productify.live", "https://www.productify.live"})
    @GetMapping("/search")
    public List<Product> searchProductsByTitle(@RequestParam String query,  @RequestParam int limit) {
        return searchService.searchProducts(query, limit);
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
        notificationService.notifyAllUsersOfScraping();
    }  

}
