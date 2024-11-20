package com.example.demo.scraping;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;


import java.net.URL;


public abstract class Scraper {
    protected WebDriver driver;

    public Scraper() {
        initializeDriver();
    }

    private void initializeDriver() {
        System.setProperty("webdriver.chrome.driver", "C:\\Development\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("--remote-debugging-port=9222");
        this.driver = new ChromeDriver(options);
    }

    protected void closeDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    protected static void downloadImage(String urlString, String destination) {
        try {
            URL url = new URL(urlString);
            ReadableByteChannel readableByteChannel = Channels.newChannel(url.openStream());
            FileOutputStream fileOutputStream = new FileOutputStream(destination);
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);
            fileOutputStream.close();
            System.out.println("Image downloaded: " + destination);
        } catch (IOException e) {
            System.out.println("Failed to download image: " + urlString);
            e.printStackTrace();
        }
    }

    // Abstract method for enforcing scraping logic in subclasses
    public abstract void initiateScraping(String categoryCSVPath);
}
