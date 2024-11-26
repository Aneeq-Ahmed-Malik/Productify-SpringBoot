package com.example.demo.scraping;

import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class AmazonScraper extends Scraper {

    private final String productLinksCSVPath = "./AmazonLinks.csv";
    private final String productDataCSVPath = "./AmazonProducts.csv";
    private final String reviewDataCSVPath = "./AmazonReviews.csv";

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
                CSVWriter writer = new CSVWriter(new FileWriter(productLinksCSVPath))) {

            List<String[]> categories;

            try {
                categories = reader.readAll(); // Can throw IOException or CsvException
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

                Set<String> uniqueLinks = new HashSet<>();

                while (true) {
                    List<WebElement> linkElements = driver.findElements(By.cssSelector(
                            ".a-link-normal.s-underline-text.s-underline-link-text.s-link-style.a-text-normal"));

                    for (WebElement element : linkElements) {
                        String link = element.getAttribute("href");
                        if (link != null && !link.isEmpty()) {
                            uniqueLinks.add(link);
                        }
                    }

                    if (uniqueLinks.size() > 110) {
                        break;
                    }

                    try {
                        WebElement nextButton = driver.findElement(By.cssSelector(
                                ".s-pagination-item.s-pagination-next.s-pagination-button.s-pagination-separator"));
                        driver.get(nextButton.getAttribute("href"));
                        Thread.sleep(2000);
                    } catch (Exception e) {
                        System.out.println("Next button not found or unable to click.");
                        break; // Exit if pagination stops
                    }
                }

                for (String link : uniqueLinks) {
                    output.add(new String[] { category, link });
                }
                writer.writeAll(output);
            }
        }
        System.out.println("Links successfully scraped and saved to " + productLinksCSVPath);
        return productLinksCSVPath;
    }

    private void getData(String ProductLinksCSVPath) {

        try (CSVReader reader = new CSVReader(new FileReader(productLinksCSVPath));
                CSVWriter writer = new CSVWriter(new FileWriter(productDataCSVPath, true));
                CSVWriter reviewWriter = new CSVWriter(new FileWriter(reviewDataCSVPath, true))) {

            List<String[]> links = new ArrayList<String[]>();
            try {
                links = reader.readAll();
            } catch (IOException | CsvException e) {
                e.printStackTrace();
                System.out.println("Error reading CSV file: " + productLinksCSVPath);
            }

            List<String[]> output = new ArrayList<>();
            output.add(new String[] { "Id", "Category", "Title", "Description", "Price", "Link", "Rating", "Image_1",
                    "Image_2", "Image_3", "Image_4" });

            List<String[]> reviewOutput = new ArrayList<>();
            output.add(new String[] { "Id", "Reviews", "Link", "Sentiment" });

            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(5));
            List<String> imageUrls = new ArrayList<>();
            String saveDirectory = "./products/";

            String price = "0";
            String title = "";
            String description = "";
            String rating = "";

            int id = 1;
            for (String[] row : links.subList(1, links.size())) { // Skip header
                String category = row[0];
                String link = row[1];

                driver.get(link);

                // Use explicit waits for elements to ensure the page is fully loaded
                try {
                    WebElement titleElement = wait
                            .until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));

                    List<WebElement> imageElements = driver
                            .findElements(By.cssSelector(".a-spacing-small.item.imageThumbnail.a-declarative"));

                    int imgcount = 1;

                    for (int i = 0; i < 4; i++) {
                        WebElement thumbnail = imageElements.get(i);
                        thumbnail.click();
                        List<WebElement> imagewrap = driver.findElements(By.cssSelector(".imgTagWrapper img"));

                        Thread.sleep(2000);
                        String imageUrl = imagewrap.get(i).getAttribute("src");
                        imageUrls.add("./products/" + category + id + "." + imgcount + ".png");

                        downloadImage(imageUrl, saveDirectory + category + id + "." + imgcount + ".png");
                        imgcount++;
                    }

                    // Try to find the optional elements
                    description = "Description not available";
                    try {
                        List<WebElement> descElements = driver
                                .findElements(By.cssSelector(".a-spacing-mini .a-list-item"));

                        StringBuilder descriptionBuilder = new StringBuilder();
                        for (WebElement element : descElements) {
                            descriptionBuilder.append(element.getText()).append(" ");
                        }
                        description = descriptionBuilder.toString().trim().replace("?", "");

                    } catch (Exception e) {
                        System.out.println("Description not found for product.");
                    }

                    rating = "0";
                    try {
                        WebElement ratingElement = driver.findElement(
                                By.xpath("//*[@id=\"cm_cr_dp_d_rating_histogram\"]/div[2]/div/div[2]/div/span/span"));
                        rating = ratingElement.getText();
                    } catch (Exception e) {
                        System.out.println("rating not found.");
                    }

                    price = "Price not available";
                    try {
                        WebElement priceWholeElement = driver.findElement(By.className("a-price-whole"));
                        WebElement priceDecimalElement = driver.findElement(By.className("a-price-fraction"));
                        price = priceWholeElement.getText() + '.' + priceDecimalElement.getText();
                    } catch (Exception e) {
                        System.out.println("Price not found for product.");
                    }

                    title = titleElement.getText();
                    System.out.println("Title" + title);
                    System.out.println("Price" + price);

                } catch (Exception e) {
                    System.out.println("Could not retrieve data for link: " + link);
                    e.printStackTrace();
                }

                while (imageUrls.size() < 4) {
                    imageUrls.add("");
                }

                output.add(new String[] {
                        String.valueOf(id),
                        category,
                        title.replace(",", ""),
                        description,
                        price,
                        link,
                        rating,
                        imageUrls.get(0),
                        imageUrls.get(1),
                        imageUrls.get(2),
                        imageUrls.get(3)
                });

                imageUrls.clear();
                title = "";
                description = "";
                price = "";
                rating = "";

                writer.writeAll(output);

                List<WebElement> traElements = driver.findElements(By.xpath("//*[@id=\"cr-single-translate\"]/span/a"));

                for (WebElement translateButton : traElements) {
                    translateButton.click();
                }

                List<WebElement> reviewElements = driver.findElements(By.cssSelector(
                        ".a-expander-content.reviewText.review-text-content.a-expander-partial-collapse-content"));

                StringBuilder combinedReviews = new StringBuilder();
                for (WebElement reviewElement : reviewElements) {
                    combinedReviews.append(reviewElement.getText().replace("\n", " ").replace(",", "")).append(" ");
                }

                String combine = combinedReviews.toString().trim().replace("?", "");

                reviewOutput.add(new String[] {
                        String.valueOf(id),
                        combine.replace(",", ""),
                        link,
                        "NR"
                });

                reviewWriter.writeAll(reviewOutput);
                id++;
            }

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error reading CSV file: " + productLinksCSVPath);
        }
    }

}
