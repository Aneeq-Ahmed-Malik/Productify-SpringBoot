package com.example.demo.service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class RecommendationService {

    @Autowired
    private ProductRepository productRepository;

    private Map<String, Double> computeTF(List<String> words) {
        Map<String, Double> tfMap = new HashMap<>();
        double totalWords = words.size();

        for (String word : words) {
            tfMap.put(word, tfMap.getOrDefault(word, 0.0) + 1.0);
        }

        // Normalize by the total number of words
        tfMap.replaceAll((word, count) -> count / totalWords);

        return tfMap;
    }

    private Map<String, Double> computeIDF(List<List<String>> allDocuments) {
        Map<String, Double> idfMap = new HashMap<>();
        int totalDocs = allDocuments.size();

        for (List<String> doc : allDocuments) {
            Set<String> uniqueWords = new HashSet<>(doc);
            for (String word : uniqueWords) {
                idfMap.put(word, idfMap.getOrDefault(word, 0.0) + 1.0);
            }
        }

        // Compute IDF values
        idfMap.replaceAll((word, docCount) -> Math.log(totalDocs / docCount));

        return idfMap;
    }

    private Map<String, Double> computeTFIDF(List<String> words, Map<String, Double> idfMap) {
        Map<String, Double> tf = computeTF(words);
        Map<String, Double> tfidf = new HashMap<>();

        for (String word : tf.keySet()) {
            tfidf.put(word, tf.get(word) * idfMap.getOrDefault(word, 0.0));
        }

        return tfidf;
    }

    private double computeCosineSimilarity(Map<String, Double> vector1, Map<String, Double> vector2) {
        Set<String> allWords = new HashSet<>();
        allWords.addAll(vector1.keySet());
        allWords.addAll(vector2.keySet());

        double dotProduct = 0.0;
        double magnitude1 = 0.0;
        double magnitude2 = 0.0;

        for (String word : allWords) {
            double value1 = vector1.getOrDefault(word, 0.0);
            double value2 = vector2.getOrDefault(word, 0.0);

            dotProduct += value1 * value2;
            magnitude1 += value1 * value1;
            magnitude2 += value2 * value2;
        }

        return dotProduct / (Math.sqrt(magnitude1) * Math.sqrt(magnitude2));
    }


    public List<Product> getRecommendations(List<Long> productIds) {
    List<Product> allProducts = productRepository.findAll();

    // Combine title and description into a list of words for each product
    List<List<String>> allDocuments = allProducts.stream()
        .map(product -> tokenizeText(product.getTitle() + " " + product.getDescription()))
        .collect(Collectors.toList());


    Map<String, Double> idfMap = computeIDF(allDocuments);

    // Tfidf matrix, as Product # ID -> (Feature1, score),(Feature2, score)
    Map<Long, Map<String, Double>> tfidfVectors = new HashMap<>();
    for (int i = 0; i < allProducts.size(); i++) {
        Product product = allProducts.get(i);
        tfidfVectors.put(product.getId(), computeTFIDF(allDocuments.get(i), idfMap));
    }

    Map<Product, Double> productScores = new HashMap<>();

    for (Long productId : productIds) {
        Product inputProduct = allProducts.stream()
            .filter(product -> product.getId().equals(productId))
            .findFirst()
            .orElse(null);

        if (inputProduct == null) continue;

        Map<String, Double> inputVector = tfidfVectors.get(productId);

        List<ProductSimilarity> similarities = new ArrayList<>();
        for (Product otherProduct : allProducts) {
            if (otherProduct.getId().equals(productId)) continue;

            Map<String, Double> otherVector = tfidfVectors.get(otherProduct.getId());
            double similarity = computeCosineSimilarity(inputVector, otherVector);

            similarities.add(new ProductSimilarity(otherProduct, similarity));
        }
        
        List<ProductSimilarity> top6Similarities = similarities.stream()
            .sorted((s1, s2) -> Double.compare(s2.getSimilarity(), s1.getSimilarity()))
            .limit(6)
            .collect(Collectors.toList());


        for (ProductSimilarity similarity : top6Similarities) {
            productScores.merge(similarity.getProduct(), similarity.getSimilarity(), Double::sum);
        }
    }

    return productScores.entrySet().stream()
        .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue())) // Sort by score descending
        .limit(6) // Take top 6
        .map(Map.Entry::getKey) // Extract the Product
        .collect(Collectors.toList());
}


    private List<String> tokenizeText(String text) {
        return Arrays.asList(text.toLowerCase().split("\\s+"));
    }


    static class ProductSimilarity {
        private final Product product;
        private final double similarity;

        public ProductSimilarity(Product product, double similarity) {
            this.product = product;
            this.similarity = similarity;
        }

        public Product getProduct() {
            return product;
        }

        public double getSimilarity() {
            return similarity;
        }
    }
}
