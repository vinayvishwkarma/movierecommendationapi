package com.movies.movierecommendation.it;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.movies.movierecommendation.helper.TestHelper;
import com.movies.movierecommendation.model.Movie;
import com.movies.movierecommendation.model.value.Genre;
import com.movies.movierecommendation.repository.ApiKeyRepository;
import com.movies.movierecommendation.repository.MovieRepository;
import com.movies.movierecommendation.service.ApiKeyService;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MovieControllerIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Autowired
    private ApiKeyRepository apiKeyRepository;

    @Autowired
    private ApiKeyService apiKeyService;

    private String apiKey;

    @BeforeEach
    private void setupData() {
        TestHelper.resetTables(movieRepository, apiKeyRepository);
        prepareTestMovies();
        apiKey = getApiKey();
        movieRepository.save(new Movie("Test Movie", Genre.DRAMA, 2025));
    }

    @Test
    void testGetMovies_withAuthAndApiKey() throws Exception {
        mockMvc.perform(get("/v1/movies")
                .header("X-API-KEY", apiKey)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("Avengers: Endgame"));
    }

    @Test
    void testGetAllMovies() throws Exception {
        mockMvc.perform(get("/v1/movies")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(5))
                .andExpect(jsonPath("$[0].title").value("Avengers: Endgame"))
                .andExpect(jsonPath("$[1].title").value("The Dark Knight"))
                .andExpect(jsonPath("$[2].title").value("The Pursuit of Happyness"));
    }

    @Test
    void testGetAllMovies_emptyList() throws Exception {
        movieRepository.deleteAll(); // Ensure the DB is empty for this test
        mockMvc.perform(get("/v1/movies")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No movies found matching the criteria."));
    }

    @Test
    void testGetMovieByName_validTitle() throws Exception {
        mockMvc.perform(get("/v1/movies/{title}", "Avengers: Endgame")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].title").value("Avengers: Endgame"));
    }

    @Test
    void testGetMovieByName_invalidTitle() throws Exception {
        mockMvc.perform(get("/v1/movies/{title}", "NonExistentMovie")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("No movies found matching the criteria."));
    }

    @Test
    void testGetMovieByName_blankTitle() throws Exception {
        mockMvc.perform(get("/v1/movies/{title}", "")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Title cannot be blank"));
    }

    @Test
    void testGetMoviesByGenre_validGenre() throws Exception {
        mockMvc.perform(get("/v1/movies/genre/{genre}", "ACTION")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(2)) // We have 2 ACTION movies
                .andExpect(jsonPath("$[0].title").value("Avengers: Endgame"))
                .andExpect(jsonPath("$[1].title").value("The Dark Knight"));
    }

    @Test
    void testGetMoviesByGenre_invalidGenre() throws Exception {
        mockMvc.perform(get("/v1/movies/genre/{genre}", "INVALID")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid genre: INVALID"));
    }

    @Test
    void testGetMoviesByRatingRange_valid() throws Exception {
        mockMvc.perform(get("/v1/movies/rating/{minRating}/{maxRating}", 7.0, 9.0)
                .header("X-API-KEY", apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(4)); // Movies with rating between 7.0 and 9.0
    }

    @Test
    void testGetMoviesByRatingRange_invalid() throws Exception {
        mockMvc.perform(get("/v1/movies/rating/{minRating}/{maxRating}", 9.5, 8.0)
                .header("X-API-KEY", apiKey))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("minRating must be less than or equal to maxRating"));
    }

    @Test
    void testGetMoviesByGenreAndRating_valid() throws Exception {
        mockMvc.perform(get("/v1/movies/byGenreAndRating")
                .param("genre", "ACTION")
                .param("minRating", "7.5")
                .param("maxRating", "9.0")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1)) // Only one movie fits this range
                .andExpect(jsonPath("$[0].title").value("The Dark Knight"));
    }

    @Test
    void testGetMoviesByGenreAndRating_invalidGenre() throws Exception {
        mockMvc.perform(get("/v1/movies/byGenreAndRating")
                .param("genre", "INVALID")
                .param("minRating", "5.0")
                .param("maxRating", "8.0")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid genre: INVALID"));
    }

    @Test
    void testGetMoviesByGenreAndRating_invalidRange() throws Exception {
        mockMvc.perform(get("/v1/movies/byGenreAndRating")
                .param("genre", "ACTION")
                .param("minRating", "8.0")
                .param("maxRating", "7.5")
                .header("X-API-KEY", apiKey))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("minRating must be less than or equal to maxRating"));
    }

    private String getApiKey() {
        return apiKeyService.generateAndStoreApiKey("IT").getApiKey();
    }

    public void prepareTestMovies() {
        Movie movie1 = new Movie();
        movie1.setTitle("Avengers: Endgame");
        movie1.setGenre(Genre.ACTION);
        movie1.setRating(8.4);
        Movie movie2 = new Movie();
        movie2.setTitle("The Dark Knight");
        movie2.setGenre(Genre.ACTION);
        movie2.setRating(9.0);
        Movie movie3 = new Movie();
        movie3.setTitle("The Pursuit of Happyness");
        movie3.setGenre(Genre.DRAMA);
        movie3.setRating(8.0);
        Movie movie4 = new Movie();
        movie4.setTitle("The Conjuring");
        movie4.setGenre(Genre.HORROR);
        movie4.setRating(7.5);
        Movie movie5 = new Movie();
        movie5.setTitle("Inception");
        movie5.setGenre(Genre.SCI_FI);
        movie5.setRating(8.8);
        movieRepository.saveAll(Arrays.asList(movie1, movie2, movie3, movie4, movie5));
    }
}
