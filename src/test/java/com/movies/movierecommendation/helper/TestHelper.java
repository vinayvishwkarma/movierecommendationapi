package com.movies.movierecommendation.helper;

import org.springframework.data.repository.CrudRepository;

public class TestHelper {

    /**
     * Resets the database tables by deleting all records.
     *
     * @param repositories the repositories to reset
     * @param <T>       the type of the repository
     */
    public static <T extends CrudRepository<?, ?>> void resetTables(T... repositories) {
        for (T repository : repositories) {
            repository.deleteAll();
        }
    }
}
