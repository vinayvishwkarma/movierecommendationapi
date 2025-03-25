package com.movies.movierecommendation.exception;

public class InvalidGenreException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public InvalidGenreException(final String message) {
        super(message);
    }

    public InvalidGenreException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
