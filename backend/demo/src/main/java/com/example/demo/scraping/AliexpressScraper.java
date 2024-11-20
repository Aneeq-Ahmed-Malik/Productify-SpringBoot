package com.example.demo.scraping;

public class AliexpressScraper extends Scraper {
    @Override
    public void initiateScraping(String categoryCSVPath){
        String productLinksCSVPath = this.getLinks(categoryCSVPath);
        this.getData(productLinksCSVPath);

    }

    private String getLinks(String categoryCSVPath){

        return "./AliexpressLinks";
    }

    private void getData(String ProductLinksCSVPath){
        
    }

}
