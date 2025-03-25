package com.movies.movierecommendation.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movies.movierecommendation.dto.ApiKeyDTO;
import com.movies.movierecommendation.service.ApiKeyService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "basicAuth")
@RequestMapping("/admin/apikey")
public class ApiKeyController {

    @Autowired
    private ApiKeyService apiKeyService;

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/register", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiKeyDTO> registerService(@RequestParam String serviceName) {
        ApiKeyDTO apiKey = apiKeyService.generateAndStoreApiKey(serviceName);
        return ResponseEntity.ok(apiKey);
    }

    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/regenerate", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiKeyDTO> regenerateApiKey(@RequestParam String serviceName) {
        ApiKeyDTO newApiKey = apiKeyService.regenerateApiKey(serviceName);
        return ResponseEntity.ok(newApiKey);
    }
}
