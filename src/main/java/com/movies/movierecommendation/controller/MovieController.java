package com.movies.movierecommendation.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movies.movierecommendation.dto.MovieDTO;
import com.movies.movierecommendation.service.MovieService;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

@RestController
@RequestMapping("/v1/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(final MovieService movieService) {
        this.movieService = movieService;
    }

    /**
     * Get movies by genre with validation.
     *
     * @param genre the genre of the movies.
     * @return List of MovieDTO.
     */
    @GetMapping("/genre/{genre}")
    public ResponseEntity<List<MovieDTO>> getMoviesByGenre(
            final @PathVariable @NotBlank(message = "Genre must not be empty") String genre) {
        List<MovieDTO> movies = movieService.getMoviesByGenre(genre);
        return getResponse(movies);
    }

    /**
     * Get movies by genre and rating with validation.
     *
     * @param genre  the genre of the movies.
     * @param rating the minimum rating.
     * @return List of MovieDTO.
     */
    @GetMapping("/genre/{genre}/rating/{rating}")
    public ResponseEntity<List<MovieDTO>> getMoviesByGenreAndRating(
            final @PathVariable @NotBlank(message = "Genre must not be empty") String genre,
            final @PathVariable @Min(value = 0, message = "Rating must be a positive number") double rating) {
        List<MovieDTO> movies = movieService.getMoviesByGenreAndRating(genre, rating);
        return getResponse(movies);
    }

    /**
     * Get a list of all movies (no filters).
     *
     * @return List of MovieDTO.
     */
    @GetMapping
    public ResponseEntity<List<MovieDTO>> getAllMovies() {
        final List<MovieDTO> movies = movieService.getAllMovies();
        return getResponse(movies);
    }

    /**
     * Common response handler for lists of movies.
     *
     * @param movies List of MovieDTO.
     * @return ResponseEntity with list or no content status.
     */
    private ResponseEntity<List<MovieDTO>> getResponse(final List<MovieDTO> movies) {
        if (movies.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.ok(movies);
    }
}
