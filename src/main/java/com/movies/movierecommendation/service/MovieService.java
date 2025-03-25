package com.movies.movierecommendation.service;

import java.util.Collection;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.movies.movierecommendation.dto.MovieDTO;
import com.movies.movierecommendation.model.Movie;
import com.movies.movierecommendation.model.value.Genre;

public interface MovieService {

    /**
     * Fetch all movies (no filtering).
     * @return a list of all MovieDTO objects.
     */
    Page<MovieDTO> getAllMovies(Pageable pageable);

    /**
     * Fetch movies by title.
     * @param title the title of the movie.
     * @return a list of MovieDTO objects.
     */
    List<MovieDTO> getMovieByTitle(String title);

    /**
     * Fetch movies by genre.
     * @param genre the genre of the movies.
     * @return a list of MovieDTO objects.
     */
    List<MovieDTO> getMoviesByGenre(Genre genre);

    /**
     * Fetch movies by rating range.
     * @param minRating the minimum rating.
     * @param maxRating the maximum rating.
     * @return a list of MovieDTO objects.
     */
    List<MovieDTO> getMoviesByRatingRange(double minRating, double maxRating);

    /**
     * Fetch movies by genre and a rating range.
     * @param genre the genre of the movies. If the genre is null, all genres are considered.
     * @param minRating the minimum rating.
     * @param maxRating the maximum rating.
     * @return a list of MovieDTO objects.
     */
    List<MovieDTO> getMoviesByGenreAndRating(Genre genre, double minRating, double maxRating);

    /**
     * Update a collection of movies.
     * @param movies the collection of movies to update.
     */
    void updateMovies(Collection<Movie> movies);
}
