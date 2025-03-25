package com.movies.movierecommendation.controller;

import java.sql.Connection;
import java.util.Map;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ping")
public class PingController {

    private static final Logger logger = LoggerFactory.getLogger(PingController.class);

    @Autowired
    private DataSource dataSource;

    @GetMapping
    public ResponseEntity<Map<String, String>> ping() {
        try (Connection connection = dataSource.getConnection()) {
            logger.info("Database connection successful.");
            return ResponseEntity.ok(Map.of("service", "up", "database", "connected"));
        } catch (Exception e) {
            logger.error("Database connection failed: " + e.getMessage(), e);
            return ResponseEntity.internalServerError().body(Map.of("service", "up", "database", "down", "error", e.getMessage()));
        }
    }
}
