package org.rzldev.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@QuarkusTest
public class MovieTest {

    static Logger LOGGER = Logger.getLogger(MovieTest.class);

    Movie movie;

    @BeforeEach
    void init() {
        movie = Movie.builder()
                .id(2L)
                .title("Garfield")
                .description("Garfield description.")
                .director("Someone")
                .country("Somewhere")
                .build();
    }

    @Test
    void testMovieData() {
        assertNotNull(movie);
        assertEquals(movie.getId(), 2L);
        assertEquals(movie.getTitle(), "Garfield");
        assertEquals(movie.getDescription(), "Garfield description.");
        assertEquals(movie.getDirector(), "Someone");
        assertEquals(movie.getCountry(), "Somewhere");

        movie.setTitle("Iron Man");
        movie.setDescription("Iron Man description.");
        movie.setDirector("Jon Favreau");
        movie.setCountry("USA");

        assertEquals(movie.getId(), 2L);
        assertEquals(movie.getTitle(), "Iron Man");
        assertEquals(movie.getDescription(), "Iron Man description.");
        assertEquals(movie.getDirector(), "Jon Favreau");
        assertEquals(movie.getCountry(), "USA");
    }
}
