package com.example.demo.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Ad;
import com.example.demo.model.User;
import com.example.demo.repository.AdRepository;
import com.example.demo.repository.UserRepository;


@Service
public class AdServices {

    @Autowired
    private AdRepository adRepository;

    @Autowired
    private UserRepository userRepository;  // Inject the EntityManager

    public Long postAd(String phoneNo,String title, String desc, Long userId, String location, Long price, 
                   MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4) {

        // Retrieve the User object from the database using the userId
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get(); // May throw an exception if empty
        
        // Create the Ad object and set its fields
        Ad ad = new Ad(user, title, desc, phoneNo, price, location);

        // Save images to the assets directory
        String imageDirectory = "/PRODUCTIFY-SPRINGBOOT/frontend/src/assets/adImages/";
        try {
            Files.createDirectories(Paths.get(imageDirectory));

        } catch (Exception e) {
            e.printStackTrace();
        }
        // List of images
        MultipartFile[] images = {image1, image2, image3, image4};

        Path[] imagePath = new Path[4];  // Array of size 4
        // Loop through images and save them if they are not null
        for (int i = 0; i < images.length; i++) {
            if (images[i] != null && !images[i].isEmpty()) {
                String imageFileName = title + "_" + userId + "_" + ad.getId() + "_" + (i + 1) + ".png";
                imagePath[i]= Paths.get(imageDirectory + imageFileName);
                try {
                    Files.write(imagePath[i], images[i].getBytes());
                } 
                catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        ad.setImage1(imagePath[0].toString());
        ad.setImage2(imagePath[1].toString());
        ad.setImage3(imagePath[2].toString());
        ad.setImage4(imagePath[3].toString());
        adRepository.save(ad);

        // Return the saved ad's ID
        return ad.getId();
    }        
    
    public List<Ad> getAllAds(){
        return adRepository.findAll();
    }

    public List<Ad> getAdsByUserEmail(String email) {
        User user = userRepository.findByEmail(email);
        if (user != null)
            return adRepository.findByUser(user);
        else 
            return null;  // Or throw an exception if user not found
    } 
}
