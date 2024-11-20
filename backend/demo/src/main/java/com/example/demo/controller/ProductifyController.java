package com.example.demo.controller;

import com.example.demo.model.Product;
import com.example.demo.scraping.Scraper;
import com.example.demo.scraping.ScraperFactory;
import com.example.demo.service.CSVImportService;
import com.example.demo.service.ProductRetrevalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductifyController {

    @Autowired
    private CSVImportService csvImportService;
    @Autowired
    private ProductRetrevalService productRetrievalService;

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
    @GetMapping("/category/{categoryName}")
    public List<Product> getProductsByCategory(@PathVariable String categoryName) {
        return productRetrievalService.getProductsByCategory(categoryName);
    }

    @GetMapping("/website/{websiteName}")
    public List<Product> getProductsByWebsite(@PathVariable String websiteName) {
        return productRetrievalService.getProductsByWebsite(websiteName);
    }

    @GetMapping("/search")
    public List<Product> searchProductsByTitle(@RequestParam String title) {
        return productRetrievalService.searchProductsByTitle(title);
    }

    @GetMapping("/price-range")
    public List<Product> getProductsByPriceRange(@RequestParam double minPrice, @RequestParam double maxPrice) {
        return productRetrievalService.getProductsByPriceRange(minPrice, maxPrice);
    }

    @GetMapping("/scrape")
    public String scrape(@RequestParam String platform, @RequestParam String categoryFileName) {
        try {
            Scraper scraper = ScraperFactory.getInstance().getScraper(platform);

            String categoryCSVPath = Paths.get("demo/src/main/resources/files", categoryFileName).toString();

            // Trigger asynchronous scraping
            initiateScrapingAsync(scraper, categoryCSVPath);
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
}
