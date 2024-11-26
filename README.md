# E-Commerce Scraper and Product Recommendation System

This project is a web application that scrapes product data from multiple eCommerce platforms (AliExpress, Amazon, Newegg) and provides product recommendations and reviews. It is built with a **Spring Boot** backend, **Angular** frontend, and integrates scraping services to retrieve product details, reviews, and images.

## Features

- **Product Scraping**: Automatically scrapes product data (name, description, price, rating, images) from eCommerce websites (AliExpress, Amazon, Newegg).
- **Product Recommendations**: Provides personalized product recommendations based on user preferences, such as shopping cart contents.
- **Review Analysis**: Analyzes product reviews and stores data for each product.
- **User Management**: Allows users to manage their profiles and shopping cart.
- **Database Integration**: Stores product data, user information, and reviews in a MySQL database.
- **CSV Import**: Supports importing bulk product data from CSV files.

## Tech Stack

- **Frontend**: Angular
- **Backend**: Spring Boot (Java)
- **Database**: MySQL
- **Scraping**: Selenium WebDriver (Java scripts for scraping data from AliExpress, Amazon, Newegg)
- **Other**: REST APIs for scraping services, CSV import for bulk data processing

