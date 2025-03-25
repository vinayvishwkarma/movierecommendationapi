package com.movies.movierecommendation.service;

import com.movies.movierecommendation.dto.ApiKeyDTO;

public interface ApiKeyService {

    /**
     * Checks if the provided API key is valid.
     *
     * @param apiKey the API key to validate
     * @return true if the API key is valid, false otherwise
     */
    boolean isValidApiKey(String apiKey);

    /**
     * Generates and stores a new API key for the specified service.
     *
     * @param serviceName the name of the service for which to generate the API key
     * @return ApiKey object containing the generated API key
     */
    ApiKeyDTO generateAndStoreApiKey(String serviceName);

    /**
     * Regenerates a new API key for the specified service.
     *
     * @param serviceName the name of the service for which to regenerate the API key
     * @return the new API key
     */
    ApiKeyDTO regenerateApiKey(String serviceName);
}
