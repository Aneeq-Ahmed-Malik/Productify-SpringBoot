package com.example.demo.scraping;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class NewggScraper extends Scraper {

    private final String productLinksCSVPath = "./NeweggLinks.csv"; // Path for product links
    private final String productDataCSVPath = "./NeweggProducts.csv"; // Path for product data

    @Override
    public void initiateScraping(String categoryCSVPath) {
        try {
            String productLinksPath = this.getLinks(categoryCSVPath);
            this.getData(productLinksPath);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeDriver(); 
        }
    }


    private String getLinks(String categoryCSVPath) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(categoryCSVPath));
            CSVWriter writer = new CSVWriter(new FileWriter(productLinksCSVPath))) {

            List<String[]> categories;
            try {
                categories = reader.readAll(); // Can throw IOException or CsvException
            } catch (CsvException e) {
                throw new RuntimeException("Error while reading the CSV file: " + categoryCSVPath, e);
            }

            List<String[]> output = new ArrayList<>();
            output.add(new String[]{"Category", "Product_link"});

            for (String[] row : categories) {
                String category = row[0];
                String url = row[1];

                driver.get(url);
                List<WebElement> products = driver.findElements(By.className("item-cell"));

                for (WebElement product : products) {
                    String link = product.findElement(By.tagName("a")).getAttribute("href");
                    output.add(new String[]{category, link});
                }
            }

            writer.writeAll(output); 
        }
        System.out.println("Links successfully scraped and saved to " + productLinksCSVPath);
        return productLinksCSVPath;
    }


    private void getData(String productLinksCSVPath) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(productLinksCSVPath));
             CSVWriter writer = new CSVWriter(new FileWriter(productDataCSVPath, true))) {

            List<String[]> links = new ArrayList<String[]>();
            try {
                links = reader.readAll(); 
            } catch (IOException | CsvException e) {
                e.printStackTrace();
                System.out.println("Error reading CSV file: " + productLinksCSVPath);
            }
            List<String[]> output = new ArrayList<>();
            output.add(new String[]{"Id", "Category", "Title", "Description", "Price", "Link", "Rating", "Image_1", "Image_2", "Image_3", "Image_4"});

            int id = 1;
            for (String[] row : links.subList(1, links.size())) { // Skip header
                String category = row[0];
                String url = row[1];

                driver.get(url);

                String title = driver.findElement(By.tagName("h1")).getText();
                String price = driver.findElement(By.className("price-current")).getText();
                String description = "";
                try {
                    description = driver.findElement(By.className("product-bullets")).getText();
                } catch (Exception ignored) {}

                String rating = "";
                try {
                    rating = driver.findElement(By.cssSelector(".product-rating *:first-child"))
                            .getAttribute("title").split(" ")[0];
                } catch (Exception ignored) {}

                List<WebElement> images = driver.findElements(By.cssSelector(".swiper-wrapper .swiper-slide img"));
                String[] imageLinks = new String[4];
                for (int i = 0; i < Math.min(images.size(), 4); i++) {
                    imageLinks[i] = images.get(i).getAttribute("src");

                    // Download the image
                    String imageFilename = String.format("./productsNewgg/Item_%d.%d.png", id, i + 1);
                    downloadImage(imageLinks[i], imageFilename);
                }

                output.add(new String[]{
                        String.valueOf(id),
                        category,
                        title,
                        description,
                        price,
                        url,
                        rating,
                        imageLinks[0], imageLinks[1], imageLinks[2], imageLinks[3]
                });
                id++;
            }

            writer.writeAll(output); // Save product details to CSV
        }
        System.out.println("Product details successfully scraped and saved to " + productDataCSVPath);
    }
}
