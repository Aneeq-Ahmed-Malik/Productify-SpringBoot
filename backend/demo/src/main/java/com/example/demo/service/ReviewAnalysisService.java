package com.example.demo.service;

import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.sentiment.SentimentCoreAnnotations;
import edu.stanford.nlp.util.CoreMap;
import edu.stanford.nlp.ling.CoreAnnotations;

import com.example.demo.model.Review;
import com.example.demo.repository.ReviewRepository;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewAnalysisService {

    @Autowired
    private ReviewRepository reviewRepository;

    public String analyzeReviews() {
        List<Review> reviews = reviewRepository.findAll();

        Properties props = new Properties();
        props.setProperty("annotators", "tokenize,pos,parse,sentiment");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);

        for (Review review : reviews) {
            if (review.getSentiment().equals("NR")) {
                String sentiment = analyzeSentiment(pipeline, review.getReviews());
                review.setSentiment(sentiment);
            }
        }

        reviewRepository.saveAll(reviews);
        
        return "Reviews analyzed Successfully!";
    }

    public String getSentiment(String link){
        return reviewRepository.findByLink(link).getSentiment();
    }

    private static String analyzeSentiment(StanfordCoreNLP pipeline, String text) {
        // Annotate the text
        Annotation annotation = new Annotation(text);
        pipeline.annotate(annotation);

        int totalScore = 0;
        int sentenceCount = 0;

        for (CoreMap sentence : annotation.get(CoreAnnotations.SentencesAnnotation.class)) {
            String sentiment = sentence.get(SentimentCoreAnnotations.SentimentClass.class);
            totalScore += getSentimentScore(sentiment);
            sentenceCount++;
        }

        double averageScore = (double) totalScore / sentenceCount;
        return getSentimentFromScore(averageScore);
    }

    private static int getSentimentScore(String sentiment) {
        switch (sentiment.toLowerCase()) {
            case "very positive":
                return 2;
            case "positive":
                return 1;
            case "neutral":
                return 0;
            case "negative":
                return -1;
            case "very negative":
                return -2;
            default:
                return 0;
        }
    }

    private static String getSentimentFromScore(double score) {
        if (score >= 1.5)
            return "Very Positive";
        else if (score >= 0.5)
            return "Positive";
        else if (score > -0.5)
            return "Neutral";
        else if (score > -1.5)
            return "Negative";
        else
            return "Very Negative";
    }

}
