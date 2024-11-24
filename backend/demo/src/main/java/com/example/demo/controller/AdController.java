package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Ad;
import com.example.demo.repository.AdRepository;
import com.example.demo.service.AdServices;
import com.example.demo.service.UserService;

@RestController
@RequestMapping("/api/ad")
public class AdController {

    @Autowired
    private AdServices adServices;

    @Autowired
    private UserService userServices;

    @Autowired
    private AdRepository adRepository; // Assuming you have an AdRepository to save the Ad to the database

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
            @RequestPart(required = false) MultipartFile image4) throws IOException {

        // Log details
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);

        String uploadDir = "frontend/src/assets/uploads";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Ad ad = new Ad();
        ad.setUser(userServices.getUserById(userId).get()); // Assuming you have a User entity to fetch or create user
        ad.setTitle(title);
        ad.setDescription(description);
        ad.setPhoneNo(phoneNo);
        ad.setPrice(Long.parseLong(price));
        ad.setLocation(location);

        ad = adRepository.save(ad);

        Map<String, String> filePaths = new HashMap<>();
        if (image1 != null) {
            String filePath = saveFile(uploadDir, image1, userId, ad.getId(), 1);
            filePaths.put("image1", "/uploads/" + filePath);
            ad.setImage1(filePath); 
        }
        if (image2 != null) {
            String filePath = saveFile(uploadDir, image2, userId, ad.getId(), 2);
            filePaths.put("image2", "/uploads/" + filePath);
            ad.setImage2(filePath); 
        }
        if (image3 != null) {
            String filePath = saveFile(uploadDir, image3, userId, ad.getId(), 3);
            filePaths.put("image3", "/uploads/" + filePath);
            ad.setImage3(filePath); 
        }
        if (image4 != null) {
            String filePath = saveFile(uploadDir, image4, userId, ad.getId(), 4);
            filePaths.put("image4", "/uploads/" + filePath);
            ad.setImage4(filePath); 
        }

        adRepository.save(ad);

        filePaths.forEach((key, value) -> System.out.println(key + ": " + value));

        Map<String, String> response = new HashMap<>();
        response.put("message", "Ad posted successfully!");
        response.putAll(filePaths);

        return ResponseEntity.ok(response);
    }

    private String saveFile(String uploadDir, MultipartFile file, Long userId, Long postId, int imageNo)
            throws IOException {
        String fileName = userId + "_" + postId + "_" + imageNo + getFileExtension(file);
        String filePath = uploadDir + File.separator + fileName;

        Files.copy(file.getInputStream(), Paths.get(filePath), StandardCopyOption.REPLACE_EXISTING);

        return fileName; 
    }

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }

    @GetMapping("/getAllAds")
    public List<Ad> getAllAds() {
        return adServices.getAllAds();
    }

    // @GetMapping("/getAdsById")
    // public List<Ad> getAdsById(@RequestParam Long user_id) {
    // return adServices.getAdsByUserId(user_id);
    // }

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
