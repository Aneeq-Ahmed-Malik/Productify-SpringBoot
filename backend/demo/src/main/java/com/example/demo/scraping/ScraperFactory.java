package com.example.demo.scraping;

public class ScraperFactory {
    private static ScraperFactory instance;

    private ScraperFactory() {}

    public static ScraperFactory getInstance() {
        if (instance == null) {
            instance = new ScraperFactory();
        }
        return instance;
    }

    public Scraper getScraper(String platform) {
        switch (platform.toLowerCase()) {
            case "amazon":
                return new AmazonScraper();
            case "newgg":
                return new NewggScraper();
            case "aliexpress":
                return new AliexpressScraper();
            default:
                throw new IllegalArgumentException("Unknown platform: " + platform);
        }
    }
}
