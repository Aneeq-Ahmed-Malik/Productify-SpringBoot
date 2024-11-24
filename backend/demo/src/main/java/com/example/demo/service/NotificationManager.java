package com.example.demo.service;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;

import jakarta.mail.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

@Service
public class NotificationManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void notifyAllUsers() {
        List<User> users = userRepository.findAll();

        for (User user : users) {
            sendNotificationEmail(user);
        }
    }

    private void sendNotificationEmail(User user) {
        // Prepare email content
        String subject = "New Products Scraped! 🎉";
        String text = "Hello " + user.getName() + ",\n\nWe have new products available for you to check out!\n\nRegards,\nYour Productify Team";

        try {
            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            System.out.println(user.getEmail());
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setText(text);
            helper.setFrom("no-reply@productify.com", "Productify Team");

            // Send the email
            mailSender.send(message);
            System.out.println("Notification sent to " + user.getEmail());

        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
}
