package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Ad;
import com.example.demo.model.User;

@Service
public class AdServices {

    @Autowired
    private EntityManager entityManager;  // Inject the EntityManager

    public Long saveAd(String title, String desc, String userId, String location, String price, 
                   MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4) {
        try {
            // Retrieve the User object from the database using the userId
            User user = getUserById(userId);

            // Create the Ad object and set its fields
            Ad ad = new Ad();
            ad.setTitle(title);
            ad.setDescription(desc);
            ad.setUser(user);  // Set the User object
            ad.setLocation(location);
            ad.setPrice(price);

            // Save images to the assets directory
            String imageDirectory = "/PRODUCTIFY-SPRINGBOOT/frontend/src/assets/adImages/";
            Files.createDirectories(Paths.get(imageDirectory));

            // List of images
            MultipartFile[] images = {image1, image2, image3, image4};

            // Loop through images and save them if they are not null
            for (int i = 0; i < images.length; i++) {
                if (images[i] != null && !images[i].isEmpty()) {
                    String imageFileName = title + "_" + userId + "_" + ad.getId() + "_" + (i + 1) + ".png";
                    Path imagePath = Paths.get(imageDirectory + imageFileName);
                    Files.write(imagePath, images[i].getBytes());
                }
            }

            // Return the saved ad's ID
            return ad.getId();
        } 
        catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Error saving images", e);
        }
    }




    private User getUserById(String userId) {
        // Query to get the User by userId
        String query = "SELECT u FROM User u WHERE u.id = :userId";
        TypedQuery<User> typedQuery = entityManager.createQuery(query, User.class);
        typedQuery.setParameter("userId", userId);
        return typedQuery.getSingleResult();  // Returns the User or throws NoResultException if not found
    }
}
