package com.movies.movierecommendation.service;

import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.movies.movierecommendation.dto.MovieDTO;
import com.movies.movierecommendation.exception.InvalidGenreException;
import com.movies.movierecommendation.model.Genre;
import com.movies.movierecommendation.model.Movie;
import com.movies.movierecommendation.repository.MovieRepository;

@Service
public class MovieServiceImp implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public List<MovieDTO> getMoviesByGenre(final String genre) {
        if (!isValidGenre(genre)) {
            throw new InvalidGenreException("Invalid genre: " + genre);
        }
        return movieRepository
                .findByGenre(genre)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    @Override
    public List<MovieDTO> getMoviesByGenreAndRating(final String genre, final double rating) {
        if (!isValidGenre(genre)) {
            throw new InvalidGenreException("Invalid genre: " + genre);
        }
        return movieRepository
                .findMoviesByGenreAndRating(genre, rating)
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    private boolean isValidGenre(final String genre) {
        return Stream.of(Genre.values())
                .anyMatch(g -> g.name().equalsIgnoreCase(genre));
    }

    @Override
    public List<MovieDTO> getAllMovies() {
        return movieRepository
                .findAll()
                .stream()
                .map(this::convertToDTO)
                .toList();
    }

    MovieDTO convertToDTO(final Movie movie) {
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getGenre().name(),
                movie.getRating());
    }
}
