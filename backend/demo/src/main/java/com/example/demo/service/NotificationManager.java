package com.example.demo.service;

import com.example.demo.repository.UserRepository;

import jakarta.mail.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;
import com.example.demo.model.*;

@Service
public class NotificationManager {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    public void notifyUserOfRecommendedProducts(User user, List<Product> products) {
        String subject = "Recommended Products Just for You!🎉";
        StringBuilder content = new StringBuilder();

        content.append("<html>")
                .append("<body>")
                .append("<h1>Hi ").append(user.getName()).append(",</h1>")
                .append("<p>We've handpicked some amazing products just for you:</p>")
                .append("<ul>");

        for (Product product : products) {
            content.append("<li>")
                    .append("<strong>").append(product.getTitle()).append("</strong><br>")
                    .append("<strong>").append("For Only $" + product.getPrice()).append("</strong><br>")
                    .append("<a href='").append(product.getLink()).append("'>View Product</a>")
                    .append("</li><br>");
        }

        content.append("</ul>")
                .append("<p>Happy Shopping!<br>Your Productify Team</p>")
                .append("</body>")
                .append("</html>");

        sendImageNotificationEmail(user, subject, content.toString());
    }

    public void notifyAllUsersOfScraping() {
        List<User> users = userRepository.findAll();
        for (User user : users) {
            String subject = "New Products Scraped! 🎉";
            String text = "Hello " + user.getName()
                    + ",\n\nWe have new products available for you to check out!\n\nRegards,\nYour Productify Team";
            sendNotificationEmail(user, subject, text);
        }
    }

    public void notifyUserOfSignUp(User user) {
        String subject = "Welcome to Productify, " + user.getName() + "! 🎉";
        String text = "Hello " + user.getName() + ",\n\n" +
                "Welcome to Productify! We’re thrilled to have you on board. 🎉\n\n" +
                "Explore a wide range of amazing products, tailored recommendations, and exciting features designed just for you.\n\n"
                +
                "Get started by logging into your account and exploring our platform:\n" +
                "If you have any questions or need assistance, feel free to reach out to us.\n\n" +
                "Thank you for choosing Productify. We’re excited to embark on this journey with you!\n\n" +
                "Warm regards,\n" +
                "The Productify Team";
        sendNotificationEmail(user, subject, text);
    }

    private void sendNotificationEmail(User user, String subject, String text) {
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

    private void sendImageNotificationEmail(User user, String subject, String text) {
        try {
            jakarta.mail.internet.MimeMessage message = mailSender.createMimeMessage();
            
            MimeMessageHelper helper = new MimeMessageHelper(message, true); // 'true' for multipart
            
            helper.setText(text, true); // true indicates it's HTML content
            
            helper.setTo(user.getEmail());
            helper.setSubject(subject);
            helper.setFrom("no-reply@productify.com", "Productify Team");
    
            // Send the email
            mailSender.send(message);
            System.out.println("Notification sent to " + user.getEmail());
    
        } catch (MessagingException | UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
    
}
