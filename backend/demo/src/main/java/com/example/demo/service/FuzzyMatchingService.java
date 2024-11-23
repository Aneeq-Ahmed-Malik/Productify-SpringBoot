package com.example.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.model.Product;
import com.example.demo.repository.ProductRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class FuzzyMatchingService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> searchProducts(String searchQuery, int limit) {
        Set<String> queryTokens = tokenizeText(searchQuery);
    
        // Calculate similarities for all products
        List<ProductSimilarity> similarities = productRepository.findAll().stream()
            .map(product -> {
                String productText = product.getTitle();
                Set<String> productTokens = tokenizeText(productText);
    
                // Combine Jaccard token similarity and Jaro-Winkler string similarity
                double tokenSimilarity = calculateTokenSimilarity(queryTokens, productTokens);
                double jaroWinkler = StringSimilarity.jaroWinklerSimilarity(searchQuery, productText);
    
                double combinedSimilarity = 0.7 * tokenSimilarity + 0.3 * jaroWinkler;
                return new ProductSimilarity(product, combinedSimilarity);
            })
            .collect(Collectors.toList());
    
        // Determine the highest similarity score
        double maxSimilarity = similarities.stream()
            .mapToDouble(ProductSimilarity::getSimilarity)
            .max()
            .orElse(0.0); // Default to 0 if no products are found
    
        // Calculate the dynamic threshold (60% of max)
        double dynamicThreshold = maxSimilarity * 0.6;
        double minThreshold = 0.1; // Minimum threshold
    
        // Filter by threshold and sort by similarity
        return similarities.stream()
            .filter(productSimilarity -> productSimilarity.getSimilarity() >= Math.max(dynamicThreshold, minThreshold))
            .sorted(Comparator.comparingDouble(ProductSimilarity::getSimilarity).reversed())
            .limit(limit)
            .map(ProductSimilarity::getProduct)
            .collect(Collectors.toList());
    }
    

    private Set<String> tokenizeText(String text) {
        return Arrays.stream(text.toLowerCase().split("\\s+"))
            .collect(Collectors.toSet());
    }

    private double calculateTokenSimilarity(Set<String> queryTokens, Set<String> productTokens) {
        Set<String> intersection = new HashSet<>(queryTokens);
        intersection.retainAll(productTokens);

        Set<String> union = new HashSet<>(queryTokens);
        union.addAll(productTokens);

        return union.isEmpty() ? 0.0 : intersection.size() / (double) union.size();
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

    public class StringSimilarity {

        public static double jaroWinklerSimilarity(String s1, String s2) {
            if (s1.equals(s2)) return 1.0;
    
            int[] matchesAndTranspositions = getMatchesAndTranspositions(s1, s2);
            int matches = matchesAndTranspositions[0];
            int transpositions = matchesAndTranspositions[1];
    
            if (matches == 0) return 0.0;
    
            double jaro = ((matches / (double) s1.length()) +
                           (matches / (double) s2.length()) +
                           ((matches - transpositions) / (double) matches)) / 3.0;
    
            // Jaro-Winkler adjustment
            int prefixLength = getPrefixLength(s1, s2);
            double jaroWinkler = jaro + (0.1 * prefixLength * (1 - jaro));
            return Math.min(jaroWinkler, 1.0);
        }
    
        private static int[] getMatchesAndTranspositions(String s1, String s2) {
            int maxDistance = Math.max(s1.length(), s2.length()) / 2 - 1;
            boolean[] s1Matches = new boolean[s1.length()];
            boolean[] s2Matches = new boolean[s2.length()];
            int matches = 0, transpositions = 0;
    
            // Match characters
            for (int i = 0; i < s1.length(); i++) {
                int start = Math.max(0, i - maxDistance);
                int end = Math.min(i + maxDistance + 1, s2.length());
                for (int j = start; j < end; j++) {
                    if (s2Matches[j] || s1.charAt(i) != s2.charAt(j)) continue;
                    s1Matches[i] = true;
                    s2Matches[j] = true;
                    matches++;
                    break;
                }
            }
    
            // Count transpositions
            int k = 0;
            for (int i = 0; i < s1.length(); i++) {
                if (!s1Matches[i]) continue;
                while (!s2Matches[k]) k++;
                if (s1.charAt(i) != s2.charAt(k)) transpositions++;
                k++;
            }
    
            return new int[]{matches, transpositions / 2};
        }
    
        private static int getPrefixLength(String s1, String s2) {
            int n = Math.min(4, Math.min(s1.length(), s2.length()));
            for (int i = 0; i < n; i++) {
                if (s1.charAt(i) != s2.charAt(i)) return i;
            }
            return n;
        }
    }
    
}
