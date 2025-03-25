package com.movies.movierecommendation.dto;

import java.time.LocalDateTime;

public class ApiKeyDTO {

    private final String serviceName;

    private final String apiKey;

    private final LocalDateTime createdAt;

    private final LocalDateTime expiryDate;

    public ApiKeyDTO(String serviceName, String apiKey, LocalDateTime createdAt,
            LocalDateTime expiryDate) {
        this.serviceName = serviceName;
        this.apiKey = apiKey;
        this.createdAt = createdAt;
        this.expiryDate = expiryDate;
    }

    public String getServiceName() {
        return serviceName;
    }

    public String getApiKey() {
        return apiKey;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
