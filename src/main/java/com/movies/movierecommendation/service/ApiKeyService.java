package com.movies.movierecommendation.service;

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
     * @return the generated API key
     */
    String generateAndStoreApiKey(String serviceName);

    /**
     * Regenerates a new API key for the specified service.
     *
     * @param serviceName the name of the service for which to regenerate the API key
     * @return the regenerated API key
     */
    String regenerateApiKey(String serviceName);
}
