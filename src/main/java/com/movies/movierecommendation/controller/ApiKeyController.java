package com.movies.movierecommendation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movies.movierecommendation.service.ApiKeyService;

@RestController
@RequestMapping("/admin/apikey")
public class ApiKeyController {

    @Autowired
    private ApiKeyService apiKeyService;

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/register")
    public ResponseEntity<String> registerService(final @RequestParam String serviceName) {
        String apiKey = apiKeyService.generateAndStoreApiKey(serviceName);
        return ResponseEntity.ok("Your API key: " + apiKey + "\n(Store it securely, you won't see it again)");
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/regenerate")
    public ResponseEntity<String> regenerateApiKey(final @RequestParam String serviceName) {
        String newApiKey = apiKeyService.regenerateApiKey(serviceName);
        return ResponseEntity.ok("New API key: " + newApiKey);
    }
}
