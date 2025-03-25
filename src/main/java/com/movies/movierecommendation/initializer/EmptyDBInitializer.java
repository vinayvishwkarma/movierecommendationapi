package com.movies.movierecommendation.initializer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.movies.movierecommendation.dto.ApiKeyDTO;
import com.movies.movierecommendation.model.Movie;
import com.movies.movierecommendation.model.value.Genre;
import com.movies.movierecommendation.service.ApiKeyService;
import com.movies.movierecommendation.service.MovieService;

import jakarta.annotation.PostConstruct;

@Component
public final class EmptyDBInitializer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmptyDBInitializer.class);

    private static final String SERVICE_NAME = "test-service";

    private static final String API_KEY_FILE_PATH = "src/main/resources/apiKey.txt";

    @Autowired
    private ApiKeyService apiKeyService;

    @Autowired
    private MovieService movieService;

    @PostConstruct
    public void init() {
        initializeApiKey();
        initializeMovieTable();
    }

    public void initializeApiKey() {
        ApiKeyDTO apiKey = apiKeyService.generateAndStoreApiKey(SERVICE_NAME);
        writeApiKeyToFile(apiKey.getApiKey());
    }

    private void writeApiKeyToFile(final String apiKey) {
        try {
            File file = new File(API_KEY_FILE_PATH);
            FileWriter fileWriter = new FileWriter(file, false);
            fileWriter.write("API Key: " + apiKey);
            fileWriter.close();
            LOGGER.info("API Key saved to file: " + file.getAbsolutePath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void initializeMovieTable() {
        List<Movie> movies = List.of(
                new Movie("Avengers: Endgame", Genre.ACTION, 8.4),
                new Movie("The Pursuit of Happyness", Genre.DRAMA, 8.0),
                new Movie("Inception", Genre.SCI_FI, 8.8),
                new Movie("Titanic", Genre.ROMANCE, 7.8),
                new Movie("The Conjuring", Genre.HORROR, 7.5),
                new Movie("The Dark Knight", Genre.ACTION, 9.0),
                new Movie("Forrest Gump", Genre.DRAMA, 8.8),
                new Movie("Interstellar", Genre.SCI_FI, 8.6),
                new Movie("The Shawshank Redemption", Genre.DRAMA, 9.3),
                new Movie("Jurassic Park", Genre.ACTION, 8.1),
                new Movie("The Matrix", Genre.SCI_FI, 8.7),
                new Movie("Fight Club", Genre.DRAMA, 8.8),
                new Movie("Inglourious Basterds", Genre.ACTION, 8.3),
                new Movie("Pulp Fiction", Genre.DRAMA, 8.9),
                new Movie("The Godfather", Genre.DRAMA, 9.2),
                new Movie("Star Wars: A New Hope", Genre.SCI_FI, 8.6),
                new Movie("The Terminator", Genre.ACTION, 8.0),
                new Movie("Schindler's List", Genre.DRAMA, 9.0),
                new Movie("The Silence of the Lambs", Genre.THRILLER, 8.6),
                new Movie("Gladiator", Genre.ACTION, 8.5),
                new Movie("Casablanca", Genre.ROMANCE, 8.5),
                new Movie("Goodfellas", Genre.DRAMA, 8.7),
                new Movie("The Departed", Genre.THRILLER, 8.5),
                new Movie("Back to the Future", Genre.SCI_FI, 8.5),
                new Movie("The Big Lebowski", Genre.COMEDY, 8.1),
                new Movie("The Avengers", Genre.ACTION, 8.1),
                new Movie("The Grand Budapest Hotel", Genre.COMEDY, 8.1),
                new Movie("The Revenant", Genre.DRAMA, 8.0),
                new Movie("Blade Runner", Genre.SCI_FI, 8.1),
                new Movie("Zombieland", Genre.COMEDY, 7.6),
                new Movie("Deadpool", Genre.COMEDY, 8.0),
                new Movie("Jumanji: Welcome to the Jungle", Genre.COMEDY, 7.0),
                new Movie("The Hangover", Genre.COMEDY, 7.7),
                new Movie("Star Wars: The Empire Strikes Back", Genre.SCI_FI, 8.7),
                new Movie("Guardians of the Galaxy", Genre.ACTION, 8.0),
                new Movie("The Truman Show", Genre.DRAMA, 8.1),
                new Movie("Django Unchained", Genre.DRAMA, 8.4),
                new Movie("Mad Max: Fury Road", Genre.ACTION, 8.1),
                new Movie("The Usual Suspects", Genre.THRILLER, 8.5),
                new Movie("A Clockwork Orange", Genre.DRAMA, 8.3),
                new Movie("The Prestige", Genre.DRAMA, 8.5),
                new Movie("Shutter Island", Genre.THRILLER, 8.2),
                new Movie("Parasite", Genre.DRAMA, 8.6),
                new Movie("Jaws", Genre.THRILLER, 8.0),
                new Movie("Se7en", Genre.THRILLER, 8.6),
                new Movie("The Shining", Genre.HORROR, 8.4),
                new Movie("American Psycho", Genre.THRILLER, 7.6),
                new Movie("The Sixth Sense", Genre.THRILLER, 8.1),
                new Movie("The Cabin in the Woods", Genre.HORROR, 7.0),
                new Movie("The Hunger Games", Genre.ACTION, 7.7),
                new Movie("Spider-Man: No Way Home", Genre.ACTION, 8.4),
                new Movie("Toy Story", Genre.ANIMATION, 8.3),
                new Movie("Finding Nemo", Genre.ANIMATION, 8.1),
                new Movie("The Lion King", Genre.ANIMATION, 8.5),
                new Movie("Frozen", Genre.ANIMATION, 7.4),
                new Movie("Coco", Genre.ANIMATION, 8.4),
                new Movie("Inside Out", Genre.ANIMATION, 8.1));
        movieService.updateMovies(movies);
        LOGGER.info("Movie table initialized with " + movies.size() + " movies.");
    }
}
