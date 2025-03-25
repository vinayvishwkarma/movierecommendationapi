package com.movies.movierecommendation.model;

import com.movies.movierecommendation.model.value.Genre;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Movie {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String title;

    @Enumerated(EnumType.STRING)
    private Genre genre;

    private double rating;

    public Movie() {}

    public Movie(final String title, final Genre genre, final double rating) {
        this.title = title;
        this.genre = genre;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(final String title) {
        this.title = title;
    }

    public Genre getGenre() {
        return genre;
    }

    public void setGenre(Genre genre) {
        this.genre = genre;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(final double rating) {
        this.rating = rating;
    }
}
