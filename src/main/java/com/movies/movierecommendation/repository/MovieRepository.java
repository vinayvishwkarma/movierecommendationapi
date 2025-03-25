package com.movies.movierecommendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movies.movierecommendation.model.Movie;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByGenre(String genre);

    @Query("SELECT m FROM Movie m WHERE m.genre = :genre AND m.rating >= :rating")
    List<Movie> findMoviesByGenreAndRating(@Param("genre") String genre, @Param("rating") double rating);
}
