package com.movies.movierecommendation.controller;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.movies.movierecommendation.dto.MovieDTO;
import com.movies.movierecommendation.exception.InvalidGenreException;
import com.movies.movierecommendation.exception.MovieNotFoundException;
import com.movies.movierecommendation.model.value.Genre;
import com.movies.movierecommendation.service.MovieService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

@SecurityRequirement(name = "X-API-KEY")
@RestController
@RequestMapping("/v1/movies")
public class MovieController {

    private final MovieService movieService;

    public MovieController(final MovieService movieService) {
        this.movieService = movieService;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Page<MovieDTO>> getAllMovies(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<MovieDTO> movies = movieService.getAllMovies(PageRequest.of(page, size));
        return ResponseEntity.ok(movies);
    }

    @GetMapping(value = "/{title}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieDTO>> getMovieByName(@PathVariable String title) {
        if (title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be blank");
        }
        final List<MovieDTO> movies = movieService.getMovieByTitle(title);
        return getResponse(movies);
    }

    @GetMapping(value = "/genre/{genre}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieDTO>> getMoviesByGenre(@PathVariable String genre) {
        try {
            if (genre.isEmpty()) {
                throw new InvalidGenreException("Genre cannot be blank");
            }
            Genre genreEnum = Genre.valueOf(genre.toUpperCase());
            List<MovieDTO> movies = movieService.getMoviesByGenre(genreEnum);
            return getResponse(movies);
        } catch (final IllegalArgumentException e) {
            throw new InvalidGenreException("Invalid genre: " + genre);
        }
    }

    @GetMapping(value = "/rating/{minRating}/{maxRating}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieDTO>> getMoviesByRatingRange(
            @PathVariable @Min(value = 0, message = "minRating must be between 0 and 10") @Max(value = 10, message = "minRating must be between 0 and 10") double minRating,
            @PathVariable @Min(value = 0, message = "maxRating must be between 0 and 10") @Max(value = 10, message = "maxRating must be between 0 and 10") double maxRating) {
        if (minRating > maxRating) {
            throw new IllegalArgumentException("minRating must be less than or equal to maxRating");
        }
        List<MovieDTO> movies = movieService.getMoviesByRatingRange(minRating, maxRating);
        return getResponse(movies);
    }

    @GetMapping(value = "/byGenreAndRating", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<MovieDTO>> getMoviesByGenreAndRating(
            @RequestParam String genre,
            @RequestParam @Min(value = 0, message = "minRating must be at least 0") @Max(value = 10, message = "minRating must not exceed 10") double minRating,
            @RequestParam @Min(value = 0, message = "maxRating must be at least 0") @Max(value = 10, message = "maxRating must not exceed 10") double maxRating) {
        if (minRating > maxRating) {
            throw new IllegalArgumentException("minRating must be less than or equal to maxRating");
        }
        Genre genreEnum;
        try {
            genreEnum = Genre.valueOf(genre.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidGenreException("Invalid genre: " + genre);
        }
        List<MovieDTO> movies = movieService.getMoviesByGenreAndRating(genreEnum, minRating, maxRating);
        return getResponse(movies);
    }

    private ResponseEntity<List<MovieDTO>> getResponse(final List<MovieDTO> movies) {
        if (movies.isEmpty()) {
            throw new MovieNotFoundException("No movies found matching the criteria.");
        }
        return ResponseEntity.ok(movies);
    }
}
