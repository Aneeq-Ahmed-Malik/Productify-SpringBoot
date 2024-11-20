package com.example.demo.scraping;

public class AmazonScraper extends Scraper {
    @Override
    public void initiateScraping(String categoryCSVPath){
        String productLinksCSVPath = this.getLinks(categoryCSVPath);
        this.getData(productLinksCSVPath);

    }

    private String getLinks(String categoryCSVPath){

        return "./AmazonLinks";
    }

    private void getData(String ProductLinksCSVPath){
        
    }

}
