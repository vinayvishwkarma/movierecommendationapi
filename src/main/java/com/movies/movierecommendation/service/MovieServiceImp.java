package com.movies.movierecommendation.service;

import java.util.Collection;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.movies.movierecommendation.dto.MovieDTO;
import com.movies.movierecommendation.model.Movie;
import com.movies.movierecommendation.model.value.Genre;
import com.movies.movierecommendation.repository.MovieRepository;

@Service
public class MovieServiceImp implements MovieService {

    @Autowired
    private MovieRepository movieRepository;

    @Override
    public Page<MovieDTO> getAllMovies(final Pageable pageable) {
        return movieRepository.findAll(pageable)
                .map(this::convertToDTO);
    }

    @Override
    public List<MovieDTO> getMovieByTitle(final String title) {
        if (StringUtils.isEmpty(title)) {
            throw new IllegalArgumentException("Title cannot be null or blank.");
        }
        return movieRepository.findByTitle(title).stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<MovieDTO> getMoviesByGenre(final Genre genre) {
        if (genre == null) {
            throw new IllegalArgumentException("Genre cannot be null.");
        }
        return movieRepository.findByGenre(genre).stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<MovieDTO> getMoviesByRatingRange(final double minRating, final double maxRating) {
        List<Movie> movies = movieRepository.findMoviesByRatingRange(minRating, maxRating);
        return movies.stream().map(this::convertToDTO).toList();
    }

    @Override
    public List<MovieDTO> getMoviesByGenreAndRating(final Genre genre, final double minRating, final double maxRating) {
        List<Movie> movies = movieRepository.findMoviesByGenreAndRatingRange(genre, minRating, maxRating);
        return movies.stream().map(this::convertToDTO).toList();
    }

    @Override
    public void updateMovies(final Collection<Movie> movies) {
        if (movies.isEmpty()) {
            throw new IllegalArgumentException("Movies collection cannot be empty.");
        }
        movieRepository.saveAll(movies);
    }

    private MovieDTO convertToDTO(final Movie movie) {
        return new MovieDTO(movie.getId(), movie.getTitle(), movie.getGenre().name(), movie.getRating());
    }
}
