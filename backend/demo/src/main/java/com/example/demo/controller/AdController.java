package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
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
import com.fasterxml.jackson.databind.ObjectMapper;

@RestController
@RequestMapping("/api/ad")
public class AdController {

    @Autowired
    private AdServices adServices;

    @CrossOrigin(origins = "http://localhost:4200")
@PostMapping("/postAd")
public ResponseEntity<?> postAd(
        @RequestPart("ad") String adJson,
        @RequestPart(value = "image1", required = false) MultipartFile image1,
        @RequestPart(value = "image2", required = false) MultipartFile image2,
        @RequestPart(value = "image3", required = false) MultipartFile image3,
        @RequestPart(value = "image4", required = false) MultipartFile image4) {
    try {
        // Debug: Log adJson and image1
        System.out.println("Ad JSON: " + adJson);
        if (image1 != null) {
            System.out.println("Image1 Filename: " + image1.getOriginalFilename());
        }

        // Convert adJson to Ad object
        ObjectMapper objectMapper = new ObjectMapper();
        Ad ad = objectMapper.readValue(adJson, Ad.class);

        // Save ad and images
        adServices.postAd(
                ad.getPhoneNo(),
                ad.getTitle(),
                ad.getDescription(),
                ad.getUser().getId(),
                ad.getLocation(),
                ad.getPrice(),
                image1, image2, image3, image4);

        return ResponseEntity.ok("Ad posted successfully!");
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to post the ad: " + e.getMessage());
    }
}

    @GetMapping("/getAllAds")
    public List<Ad> getAllAds() {
        return adServices.getAllAds();
    }

    @GetMapping("/getAdsById")
    public List<Ad> getAdsById(@RequestParam Long user_id) {
        return adServices.getAdsByUserId(user_id);
    }

    @DeleteMapping("/deleteAd")
    public String deleteAd(@RequestParam Long user_id , @RequestParam Long Ad_id ){
        return adServices.deleteAd(user_id , Ad_id);
    }

    @PostMapping("/editAd")
    public String editAd(@RequestParam Long user_id , @RequestParam Long Ad_id  , @RequestBody Ad editedAd){
        return adServices.editAd(user_id , Ad_id , editedAd);
    }

}
