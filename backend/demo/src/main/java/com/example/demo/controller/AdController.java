package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
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

    @PostMapping("/postAd")
    public String postAd(
            @RequestBody Ad ad, 
            @RequestPart(required = false) MultipartFile image1, 
            @RequestPart(required = false) MultipartFile image2,
            @RequestPart(required = false) MultipartFile image3,
            @RequestPart(required = false) MultipartFile image4) {
        try {
            // Save ad details in the database
            Long adId = adServices.postAd(
                    ad.getPhoneNo(), 
                    ad.getTitle(), 
                    ad.getDescription(),
                    ad.getUser().getId(), 
                    ad.getLocation(), 
                    ad.getPrice(),
                    image1, image2, image3, image4);
            
            return "Ad posted successfully!";
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed to post the ad: " + e.getMessage();
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
