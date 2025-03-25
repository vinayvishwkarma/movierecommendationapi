package com.movies.movierecommendation.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.movies.movierecommendation.exception.InvalidGenreException;
import com.movies.movierecommendation.exception.MovieNotFoundException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MovieNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleMovieNotFoundException(final MovieNotFoundException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Movie Not Found");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidGenreException.class)
    public ResponseEntity<Map<String, String>> handleInvalidGenreException(final InvalidGenreException ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Invalid Genre");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleException(final Exception ex) {
        Map<String, String> response = new HashMap<>();
        response.put("error", "Internal Server Error");
        response.put("message", ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
