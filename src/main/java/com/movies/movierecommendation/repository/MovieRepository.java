package com.movies.movierecommendation.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.movies.movierecommendation.model.Movie;
import com.movies.movierecommendation.model.value.Genre;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Integer> {

    List<Movie> findByGenre(Genre genre);

    @Query("SELECT m FROM Movie m WHERE LOWER(m.title) LIKE LOWER(CONCAT('%', :title, '%'))")
    List<Movie> findByTitle(@Param("title") String title);

    @Query("SELECT m FROM Movie m WHERE m.genre = :genre AND m.rating BETWEEN :minRating AND :maxRating")
    List<Movie> findMoviesByGenreAndRatingRange(@Param("genre") Genre genre, @Param("minRating") double minRating, @Param("maxRating") double maxRating);

    @Query("SELECT m FROM Movie m WHERE m.rating BETWEEN :minRating AND :maxRating")
    List<Movie> findMoviesByRatingRange(@Param("minRating") double minRating, @Param("maxRating") double maxRating);
}
