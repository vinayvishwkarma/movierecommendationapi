package com.movies.movierecommendation.dto;

public class MovieDTO {

    private final int id;

    private final String title;

    private final String genre;

    private final double rating;

    public MovieDTO(final int id, final String title, final String genre, final double rating) {
        this.id = id;
        this.title = title;
        this.genre = genre;
        this.rating = rating;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getGenre() {
        return genre;
    }

    public double getRating() {
        return rating;
    }
}
