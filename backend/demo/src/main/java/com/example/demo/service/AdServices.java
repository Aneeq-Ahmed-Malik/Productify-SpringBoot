package com.example.demo.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
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

    @Autowired
    private UserService userServices;

    public ResponseEntity<Map<String, String>> postAd(
            String title, String description, String price, String location, String phoneNo, Long userId,
            MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4)
            throws IOException {

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

    private String getFileExtension(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        return fileName != null ? fileName.substring(fileName.lastIndexOf(".")) : "";
    }

    public List<Ad> getAllAds() {
        return adRepository.findAll();
    }

    public List<Ad> getAdsByUserId(Long userId) {
        Optional<User> user = userRepository.findById(userId); // Step 1: Retrieve the user
        if (user.isPresent()) { // Step 2: Check if user exists
            return adRepository.findByUserId(user.get().getId()); // Step 3: Fetch ads for the user
        }
        return Collections.emptyList(); // Step 4: Return an empty list if no user is found
    }

    public String deleteAd(Long user_id, Long ad_id) {
        // Find the ad by its ID
        Optional<Ad> adOptional = adRepository.findById(ad_id);

        // Check if the ad exists
        if (adOptional.isPresent()) {
            Ad ad = adOptional.get();
            User user = ad.getUser();

            // Check if the ad belongs to the user
            if (user.getId().equals(user_id)) {
                // Remove the ad from the user's ads list
                user.getAds().remove(ad); // Remove the ad from the user's ads list

                // Save the updated user with the modified ads list
                userRepository.save(user);

                // Delete the images for this ad
                String prefix = String.format("%d_%d", user_id, ad_id); // Create the prefix

                try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("frontend/src/assets/uploads/"), prefix + "*")) {
                    for (Path path : stream) {
                        try {
                            Files.deleteIfExists(path); // Safely delete the file
                            System.out.println("Deleted: " + path.toString());
                        } catch (Exception e) {
                            System.err.println("Error deleting image: " + path.toString());
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    System.err.println("Error reading directory or deleting files: ");
                    e.printStackTrace();
                }

                // Delete the ad
                adRepository.delete(ad);

                return "Ad and associated images deleted successfully.";
            } else {
                return "Ad does not belong to the user.";
            }
        }

        // If ad does not exist
        return "Ad could not be found.";
    }

    public ResponseEntity<Map<String, String>> editAd(
            Long ad_id , String title, String description, String price, String location, String phoneNo, Long userId,
            MultipartFile image1, MultipartFile image2, MultipartFile image3, MultipartFile image4)
            throws IOException {

        String uploadDir = "frontend/src/assets/uploads";

        File directory = new File(uploadDir);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        Optional<Ad> adObject = adRepository.findById(ad_id);
        Ad ad = adObject.get();
        ad.setUser(userServices.getUserById(userId).get()); // Assuming you have a User entity to fetch or create user
        ad.setTitle(title);
        ad.setDescription(description);
        ad.setPhoneNo(phoneNo);
        ad.setPrice(Long.parseLong(price));
        ad.setLocation(location);
        ad.setId(ad_id);

        adRepository.save(ad);


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
        return ResponseEntity.ok(response);
    }

}
