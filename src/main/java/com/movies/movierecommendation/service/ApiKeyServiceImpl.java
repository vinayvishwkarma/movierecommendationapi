package com.movies.movierecommendation.service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movies.movierecommendation.model.ApiKey;
import com.movies.movierecommendation.repository.ApiKeyRepository;

@Service
public class ApiKeyServiceImpl implements ApiKeyService {

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Override
    public String generateAndStoreApiKey(String serviceName) {
        String rawApiKey = UUID.randomUUID() + "-" + UUID.randomUUID();
        String hashedApiKey = hashApiKey(rawApiKey);
        ApiKey apiKey = new ApiKey();
        apiKey.setApiKey(hashedApiKey);
        apiKey.setServiceName(serviceName);
        apiKey.setActive(true);
        apiKey.setExpiryDate(LocalDateTime.now().plusMonths(6));
        apiKeyRepository.save(apiKey);
        return rawApiKey;
    }

    private String hashApiKey(String apiKey) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(apiKey.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing API key", e);
        }
    }

    @Override
    public boolean isValidApiKey(final String apiKey) {
        String hashedInput = hashApiKey(apiKey);
        Optional<ApiKey> serviceApiKey = apiKeyRepository.findByApiKey(hashedInput);
        return serviceApiKey.isPresent() && serviceApiKey.get().isActive() &&
                serviceApiKey.get().getExpiryDate().isAfter(LocalDateTime.now());
    }

    public String regenerateApiKey(String serviceName) {
        Optional<ApiKey> existingKey = apiKeyRepository.findByServiceName(serviceName);
        if (existingKey.isPresent()) {
            apiKeyRepository.delete(existingKey.get());
        }
        return generateAndStoreApiKey(serviceName);
    }
}
