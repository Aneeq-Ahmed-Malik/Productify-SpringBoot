package com.example.demo.service;

import com.example.demo.model.Category;
import com.example.demo.model.Product;
import com.example.demo.model.Review;
import com.example.demo.model.Website;
import com.example.demo.repository.CategoryRepository;
import com.example.demo.repository.ProductRepository;
import com.example.demo.repository.ReviewRepository;
import com.example.demo.repository.WebsiteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.opencsv.CSVReader;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

@Service
public class CSVImportService {

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private WebsiteRepository websiteRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    public void importCategories() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/category.csv");
         CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            // Skip the first row (header)
            reader.readNext();
            
            List<String[]> rows = reader.readAll();
            rows.forEach(row -> {
                Category category = new Category();
                category.setId(Long.parseLong(row[0]));  // Assuming the ID is in the first column
                category.setName(row[1]);  // Assuming the name is in the second column
                categoryRepository.save(category);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public void importWebsites() {
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/website.csv");
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            // Skip the first row (header)
            reader.readNext();
            
            List<String[]> rows = reader.readAll();
            rows.forEach(row -> {
                Website website = new Website();
                website.setId(Long.parseLong(row[0]));  
                website.setName(row[1]);  
                websiteRepository.save(website);
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private InputStream getPath(String website) throws FileNotFoundException {
        String filePath = "";
        if (website.equals("Newgg")) {
            filePath = "files/NewggProducts.csv";
        } else if (website.equals("Amazon")) {
            filePath = "files/AmazonProducts.csv";
        } else if (website.equals("AliExpress")) {
            filePath = "files/AliExpressProducts.csv";
        } else {
            throw new FileNotFoundException("No CSV file found for website: " + website);
        }

        // Load file from classpath
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found in classpath: " + filePath);
        }
        return inputStream;
    }

    public void importProducts(String websiteName) {
        try (InputStream inputStream = getPath(websiteName);
        CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            reader.readNext();

            List<String[]> rows = reader.readAll();
            rows.forEach(row -> {
                Product product = new Product(
                row[2],  // Title
                row[3],  // Description
                Double.parseDouble(row[4]),  // Price
                row[5],  // Link
                Double.parseDouble(row[6]),  // Rating
                row[7],  // Image 1
                row[8],  // Image 2
                row[9],  // Image 3
                row[10]   // Image 4
                );
                // Handle Category (getting Category based on name)
                String categoryName = row[1];  // Category name from column 11 (index 10)
                Category category = categoryRepository.findByName(categoryName);
                product.setCategory(category);

                Website website = websiteRepository.findByName(websiteName);  // Static reference (example)
                product.setWebsite(website);

                // Save product to repository
                productRepository.save(product);
            });
        } catch (Exception e) {
            e.printStackTrace();  // Consider using logging instead of printStackTrace()
        }
    }

    private InputStream getPathForReview(String website) throws FileNotFoundException {
        String filePath = "";
        if (website.equals("Newgg")) {
            filePath = "files/NewggReviews.csv";
        } else if (website.equals("Amazon")) {
            filePath = "files/AmazonReviews.csv";
        }
        else if (website.equals("AliExpress")) {
            filePath = "files/AliExpressReviews.csv";
        } else {
            throw new FileNotFoundException("No CSV file found for website: " + website);
        }

        // Load file from classpath
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(filePath);
        if (inputStream == null) {
            throw new FileNotFoundException("File not found in classpath: " + filePath);
        }
        return inputStream;
    }

    public void importReviews(String websiteName) {
        try (InputStream inputStream = getPathForReview(websiteName);
            CSVReader reader = new CSVReader(new InputStreamReader(inputStream))) {
            reader.readNext();
            
            List<String[]> rows = reader.readAll();
            
        
            // Iterate over all rows in the CSV file
            for (String[] row : rows) {
                if (row.length >= 4) { // Check that the row has enough columns
                    Review review = new Review();
                    review.setReviews(row[1]);  // Assuming review is in column 1
                    review.setLink(row[2]);     // Assuming link is in column 2
                    review.setSentiment(row[3]);  // Assuming sentiment is in column 3
                    
                    reviewRepository.save(review);  // Save review to the database
                }
            }

            System.out.println("Total reviews imported: " + rows.size());


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    

}
