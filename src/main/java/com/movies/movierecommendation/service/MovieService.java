package com.movies.movierecommendation.service;

import java.util.List;

import com.movies.movierecommendation.dto.MovieDTO;

public interface MovieService {

    /**
     * Fetch movies by genre.
     * @param genre the genre of the movies.
     * @return a list of MovieDTO objects.
     */
    List<MovieDTO> getMoviesByGenre(String genre);

    /**
     * Fetch movies by genre and a minimum rating.
     * @param genre the genre of the movies.
     * @param rating the minimum rating.
     * @return a list of MovieDTO objects.
     */
    List<MovieDTO> getMoviesByGenreAndRating(String genre, double rating);

    /**
     * Fetch all movies (no filtering).
     * @return a list of all MovieDTO objects.
     */
    List<MovieDTO> getAllMovies();
}
