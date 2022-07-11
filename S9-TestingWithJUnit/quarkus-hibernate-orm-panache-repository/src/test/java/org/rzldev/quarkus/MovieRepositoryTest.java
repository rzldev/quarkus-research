package org.rzldev.quarkus;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
class MovieRepositoryTest {

    @Inject
    MovieRepository movieRepository;

    @Test
    void findByCountryOK() {
        List<Movie> movies = movieRepository.findByCountry("USA");
        assertNotNull(movies);
        assertFalse(movies.isEmpty());
        assertEquals(movies.size(), 2);
        assertEquals(1, movies.get(1).getId());
        assertEquals("Iron Man", movies.get(1).getTitle());
        assertEquals("Iron Man description", movies.get(1).getDescription());
        assertEquals("Jon Favreau", movies.get(1).getDirector());
        assertEquals("USA", movies.get(1).getCountry());
    }

    @Test
    void findByCountryKO() {
        List<Movie> movies = movieRepository.findByCountry("Pluto");
        assertNotNull(movies);
        assertTrue(movies.isEmpty());
        assertEquals(movies.size(), 0);
    }

    @Test
    void updateMovieOK() {
        Movie updatedMovie = new Movie();
        updatedMovie.setTitle("Iron Man 2");
        updatedMovie.setDescription("Iron Man 2 description");
        updatedMovie.setDirector("Jon Favreau");
        updatedMovie.setCountry("USA");

        Movie movie = movieRepository.updateMovie(1L, updatedMovie);
        assertNotNull(movie);
        assertEquals(1, movie.getId());
        assertEquals("Iron Man 2", movie.getTitle());
        assertEquals("Iron Man 2 description", movie.getDescription());
        assertEquals("Jon Favreau", movie.getDirector());
        assertEquals("USA", movie.getCountry());
    }

    @Test
    void updateMovieKO() {
        Movie movie = movieRepository.updateMovie(1000L, new Movie());
        assertNull(movie);
    }
}