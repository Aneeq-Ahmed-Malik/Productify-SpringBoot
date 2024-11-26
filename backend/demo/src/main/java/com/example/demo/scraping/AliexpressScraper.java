package com.example.demo.scraping;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

public class AliexpressScraper extends Scraper {

    private final String uniqueLinksCSVPath = "./AliExpresslinks.csv";
    private final String productDataCSVPath = "./AliExpressProducts.csv";
    private final String reviewDataCSVPath = "./AliExpressReviews.csv";

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
        try (InputStream inputStream = getClass().getClassLoader().getResourceAsStream("files/" + categoryCSVPath);
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                CSVReader reader = new CSVReader(inputStreamReader);
                CSVWriter writer = new CSVWriter(new FileWriter(uniqueLinksCSVPath))) {

            List<String[]> categories;
            try {
                categories = reader.readAll();
            } catch (CsvException e) {
                throw new RuntimeException("Error while reading the CSV file: " + categoryCSVPath, e);
            }

            List<String[]> output = new ArrayList<>();
            output.add(new String[] { "Category", "Product_link" });

            for (int i = 1; i < categories.size(); i++) {
                String[] row = categories.get(i);
                String category = row[0];
                String url = row[1];

                driver.get(url);
                driver.manage().window().maximize();
                smoothScroll(8.4 * driver.manage().window().getSize().getHeight(), 0.1);

                List<WebElement> productLinks = driver.findElements(By.cssSelector("#card-list > div > div > div > a"));
                for (WebElement product : productLinks) {
                    String link = product.getAttribute("href");
                    if (link != null) {
                        output.add(new String[] { category, link });
                    }
                }

                try {
                    WebElement nextButton = driver.findElement(By.cssSelector(".comet-pagination-next > button"));
                    nextButton.click();
                } catch (Exception e) {
                    System.out.println("No more pages or next button not found.");
                    break;
                }
            }

            writer.writeAll(output);
        }
        System.out.println("Links successfully scraped and saved to " + uniqueLinksCSVPath);
        return uniqueLinksCSVPath;
    }

    private void smoothScroll(double targetScrollHeight, double scrollSpeed) {
        JavascriptExecutor jsExecutor = (JavascriptExecutor) driver; // Cast driver to JavascriptExecutor
        long currentScrollHeight = (long) jsExecutor.executeScript("return window.scrollY;");

        while (currentScrollHeight < targetScrollHeight) {
            jsExecutor.executeScript("window.scrollTo(0, arguments[0]);", currentScrollHeight + 10);
            currentScrollHeight += 10;

            try {
                Thread.sleep((long) (scrollSpeed * 1000)); // Convert seconds to milliseconds
            } catch (InterruptedException ignored) {
            }
        }
    }

    private void getData(String productLinksCSVPath) throws IOException {
        try (CSVReader reader = new CSVReader(new FileReader(productLinksCSVPath));
                CSVWriter productWriter = new CSVWriter(new FileWriter(productDataCSVPath, true));
                CSVWriter reviewWriter = new CSVWriter(new FileWriter(reviewDataCSVPath, true))) {

            List<String[]> links;
            try {
                links = reader.readAll();
            } catch (CsvException e) {
                throw new RuntimeException("Error reading CSV file: " + productLinksCSVPath, e);
            }

            int productCount = 1;

            for (String[] row : links.subList(1, links.size())) { // Skip header
                String category = row[0];
                String url = row[1];
                driver.get(url);

                WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
                String title = "";
                String price = "Price not available";
                String description = "";
                double rating = 0.0;
                List<String> imageLinks = new ArrayList<>();

                try {
                    // Scrape title
                    WebElement titleElement = wait.until(ExpectedConditions
                            .visibilityOfElementLocated(By.cssSelector(".title--wrap--UUHae_g > h1")));
                    title = titleElement.getText();

                    // Scrape price
                    try {
                        WebElement priceElement = driver.findElement(By.cssSelector(".product-price-value"));
                        price = priceElement.getText();
                    } catch (Exception ignored) {
                    }

                    // Scrape images
                    List<WebElement> imageElements = driver.findElements(By.cssSelector(".slider--wrap--krlZ7X9 img"));
                    for (int i = 0; i < Math.min(imageElements.size(), 4); i++) {
                        String imageUrl = imageElements.get(i).getAttribute("src");
                        imageLinks.add(imageUrl);

                        // Call to download image
                        downloadImage(imageUrl,
                                String.format("./products/%s_%d_%d.png", category, productCount, i + 1));
                    }

                    // Scrape reviews and rating
                    smoothScroll(driver.manage().window().getSize().getHeight() * 1.5, 0.1);
                    try {
                        WebElement reviewButton = driver.findElement(By.cssSelector("#nav-review > div > button"));
                        reviewButton.click();
                        WebElement ratingElement = driver.findElement(By.cssSelector(".header--num--GaAGwoZ"));
                        rating = Double.parseDouble(ratingElement.getText());

                        List<WebElement> reviewElements = driver
                                .findElements(By.cssSelector(".list--itemReview--xQUhO78"));
                        StringBuilder reviews = new StringBuilder();
                        for (WebElement review : reviewElements) {
                            reviews.append(review.getText()).append(" ");
                        }

                        reviewWriter.writeNext(
                                new String[] { String.valueOf(productCount), reviews.toString(), url, "NR" });
                    } catch (Exception ignored) {
                    }

                    // Save product details
                    productWriter.writeNext(new String[] {
                            String.valueOf(productCount),
                            category,
                            title,
                            description,
                            price,
                            url,
                            String.valueOf(rating),
                            imageLinks.size() > 0 ? imageLinks.get(0) : "",
                            imageLinks.size() > 1 ? imageLinks.get(1) : "",
                            imageLinks.size() > 2 ? imageLinks.get(2) : "",
                            imageLinks.size() > 3 ? imageLinks.get(3) : ""
                    });

                    productCount++;
                } catch (Exception e) {
                    System.out.println("Error processing product at " + url);
                    e.printStackTrace();
                }
            }
        }
        System.out.println("Product details successfully scraped and saved to " + productDataCSVPath);
    }

}
