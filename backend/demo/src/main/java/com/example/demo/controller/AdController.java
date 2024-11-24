package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Ad;

import com.example.demo.service.AdServices;

@RestController
@RequestMapping("/api/ad")
public class AdController {

    @Autowired
    private AdServices adServices;

    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping("/postAd")
    public ResponseEntity<Map<String, String>> postAd(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("price") String price,
            @RequestParam("location") String location,
            @RequestParam("phoneNo") String phoneNo,
            @RequestParam("userId") Long userId,
            @RequestPart(required = false) MultipartFile image1,
            @RequestPart(required = false) MultipartFile image2,
            @RequestPart(required = false) MultipartFile image3,
            @RequestPart(required = false) MultipartFile image4) {

        // Log the details
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Price: " + price);
        System.out.println("Location: " + location);
        System.out.println("PhoneNo: " + phoneNo);
        System.out.println("UserId: " + userId);

        // Handle the images
        if (image1 != null) {
            System.out.println("Image 1: " + image1.getOriginalFilename());
        }
        if (image2 != null) {
            System.out.println("Image 2: " + image2.getOriginalFilename());
        }
        if (image3 != null) {
            System.out.println("Image 3: " + image3.getOriginalFilename());
        }
        if (image4 != null) {
            System.out.println("Image 4: " + image4.getOriginalFilename());
        }

        Map<String, String> response = new HashMap<>();
        response.put("message", "Ad posted successfully!");
        return ResponseEntity.ok(response);
    }

    @GetMapping("/getAllAds")
    public List<Ad> getAllAds() {
        return adServices.getAllAds();
    }

    @GetMapping("/getAdsById")
    public List<Ad> getAdsById(@RequestParam Long user_id) {
        return adServices.getAdsByUserId(user_id);
    }

    /////////////testing//////////   
    @CrossOrigin(origins = "http://localhost:4200")
    @PostMapping
    public ResponseEntity<Map<String, String>> uploadFiles(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestPart("files") List<MultipartFile> files) {

        System.out.println("Title: " + title);
        System.out.println("Description: " + description);

        if (files != null && !files.isEmpty()) {
            for (MultipartFile file : files) {
                System.out.println("Uploaded File: " + file.getOriginalFilename());
            }
        }

        // Return a JSON response
        Map<String, String> response = new HashMap<>();
        response.put("message", "Files uploaded successfully!");
        return ResponseEntity.ok(response);
    }

}
