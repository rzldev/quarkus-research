package org.rzldev.quarkus;

import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import javax.inject.Inject;
import javax.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@QuarkusTest
@Disabled
class MovieResourceJUnitTest {

    @InjectMock
    MovieRepository movieRepository;

    @Inject
    MovieResource movieResource;

    @Inject
    MovieMapper movieMapper;

    private Movie movie;

    @BeforeEach
    void setUp() {
        movie = new Movie();
        movie.setId(1L);
        movie.setTitle("Gundala");
        movie.setDescription("Gundala description.");
        movie.setDirector("Joko Anwar");
        movie.setCountry("Indonesia");
    }

    @Test
    void getAll() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        Mockito.when(movieRepository.listAll()).thenReturn(movies);

        Response response = movieResource.getAll();
        assertNotNull(response);
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());
        assertNotNull(response.getEntity());

        List<Movie> entity = (List<Movie>) response.getEntity();
        assertFalse(entity.isEmpty());
        assertEquals(entity.size(), 1);
        assertEquals(movie.getId(), entity.get(0).getId());
        assertEquals(movie.getTitle(), entity.get(0).getTitle());
        assertEquals(movie.getDescription(), entity.get(0).getDescription());
        assertEquals(movie.getDirector(), entity.get(0).getDirector());
        assertEquals(movie.getCountry(), entity.get(0).getCountry());
    }

    @Test
    void getMovieByIdOK() {
        Mockito.when(movieRepository.findByIdOptional(movie.getId()))
                .thenReturn(Optional.of(movie));

        Response response = movieResource.getMovieById(1L);
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        Movie mEntity = (Movie) response.getEntity();
        assertEquals(movie.getId(), mEntity.getId());
        assertEquals(movie.getTitle(), mEntity.getTitle());
        assertEquals(movie.getDescription(), mEntity.getDescription());
        assertEquals(movie.getDirector(), mEntity.getDirector());
        assertEquals(movie.getCountry(), mEntity.getCountry());
    }

    @Test
    void getMovieByIdKO() {
        Mockito.when(movieRepository.findByIdOptional(movie.getId()))
                .thenReturn(Optional.of(movie));

        Response response = movieResource.getMovieById(100L);
        assertNotNull(response);
        assertNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void getMovieByTitleOK() {
        PanacheQuery<Movie> query = Mockito.mock(PanacheQuery.class);
        Mockito.when(query.page(Mockito.any())).thenReturn(query);
        Mockito.when(query.singleResultOptional()).thenReturn(Optional.of(movie));

        Mockito.when(movieRepository.find("title", movie.getTitle()))
                .thenReturn(query);

        Response response = movieResource.getMovieByTitle("Gundala");
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        Movie mEntity = (Movie) response.getEntity();
        assertEquals(movie.getId(), mEntity.getId());
        assertEquals(movie.getTitle(), mEntity.getTitle());
        assertEquals(movie.getDescription(), mEntity.getDescription());
        assertEquals(movie.getDirector(), mEntity.getDirector());
        assertEquals(movie.getCountry(), mEntity.getCountry());
    }

    @Test
    void getMovieByTitleKO() {
        PanacheQuery<Movie> query = Mockito.mock(PanacheQuery.class);
        Mockito.when(query.page(Mockito.any())).thenReturn(query);
        Mockito.when(query.singleResultOptional()).thenReturn(Optional.empty());

        Mockito.when(movieRepository.find("title", "Gundam"))
                .thenReturn(query);

        Response response = movieResource.getMovieByTitle("Gundam");
        assertNotNull(response);
        assertNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void getMovieByCountryOK() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        Mockito.when(movieRepository.findByCountry(movie.getCountry()))
                .thenReturn(movies);

        Response response = movieResource.getMovieByCountry("Indonesia");
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        List<Movie> mEntities = (List<Movie>) response.getEntity();
        assertEquals(movie.getId(), mEntities.get(0).getId());
        assertEquals(movie.getTitle(), mEntities.get(0).getTitle());
        assertEquals(movie.getDescription(), mEntities.get(0).getDescription());
        assertEquals(movie.getDirector(), mEntities.get(0).getDirector());
        assertEquals(movie.getCountry(), mEntities.get(0).getCountry());
    }

    @Test
    void getMovieByCountryKO() {
        List<Movie> movies = new ArrayList<>();
        movies.add(movie);
        Mockito.when(movieRepository.findByCountry(movie.getCountry()))
                .thenReturn(movies);

        Response response = movieResource.getMovieByCountry("Pluto");
        assertNotNull(response);
        assertNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void createMovieOK() {
        Mockito.doNothing().when(movieRepository)
                .persist(ArgumentMatchers.any(Movie.class));

        Mockito.when(movieRepository.isPersistent(ArgumentMatchers.any(Movie.class)))
                .thenReturn(true);

        MovieDTO newMovie = new MovieDTO();
        newMovie.setTitle("Iron Man");
        newMovie.setDescription("Iron Man description.");
        newMovie.setDirector("Jon Favreau");
        newMovie.setCountry("USA");

        Response response = movieResource.createMovie(newMovie);
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.CREATED.getStatusCode());

        Movie mEntity = (Movie) response.getEntity();
        assertEquals(newMovie.getId(), mEntity.getId());
        assertEquals(newMovie.getTitle(), mEntity.getTitle());
        assertEquals(newMovie.getDescription(), mEntity.getDescription());
        assertEquals(newMovie.getDirector(), mEntity.getDirector());
        assertEquals(newMovie.getCountry(), mEntity.getCountry());
    }

    @Test
    void createMovieKO() {
        Mockito.doNothing().when(movieRepository)
                .persist(ArgumentMatchers.any(Movie.class));

        Mockito.when(movieRepository.isPersistent(ArgumentMatchers.any(Movie.class)))
                .thenReturn(false);

        MovieDTO newMovie = new MovieDTO();
        newMovie.setTitle("Iron Man");
        newMovie.setDescription("Iron Man description.");
        newMovie.setDirector("Jon Favreau");
        newMovie.setCountry("USA");

        Response response = movieResource.createMovie(newMovie);
        assertNotNull(response);
        assertNull(response.getEntity());
        assertEquals(response.getStatus(),
                Response.Status.BAD_REQUEST.getStatusCode());

    }

    @Test
    void updateMovieOK() {
        MovieDTO updatedMovie = new MovieDTO();
        updatedMovie.setId(movie.getId());
        updatedMovie.setTitle("Gundala 2");
        updatedMovie.setDescription("Gundala 2 description.");
        updatedMovie.setDirector("Joko Anwar");
        updatedMovie.setCountry("Indonesia");

        Mockito.when(movieRepository.findByIdOptional(movie.getId()))
                .thenReturn(Optional.of(movie));

        Response response = movieResource.updateMovie(1L, updatedMovie);
        assertNotNull(response);
        assertNotNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.OK.getStatusCode());

        Movie mEntity = (Movie) response.getEntity();
        assertEquals(updatedMovie.getId(), mEntity.getId());
        assertEquals(updatedMovie.getTitle(), mEntity.getTitle());
        assertEquals(updatedMovie.getDescription(), mEntity.getDescription());
        assertEquals(updatedMovie.getDirector(), mEntity.getDirector());
        assertEquals(updatedMovie.getCountry(), mEntity.getCountry());
    }

    @Test
    void updateMovieKO() {
        Mockito.when(movieRepository.findByIdOptional(movie.getId()))
                .thenReturn(Optional.empty());

        Response response = movieResource.updateMovie(1L, movieMapper.toDTO(new Movie()));
        assertNotNull(response);
        assertNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    void deleteMovieOK() {
        Mockito.when(movieRepository.deleteById(movie.getId()))
                .thenReturn(true);

        Response response = movieResource.deleteMovie(1L);
        assertNotNull(response);
        assertNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.NO_CONTENT.getStatusCode());
    }

    @Test
    void deleteMovieKO() {
        Mockito.when(movieRepository.deleteById(movie.getId()))
                .thenReturn(false);

        Response response = movieResource.deleteMovie(1L);
        assertNotNull(response);
        assertNull(response.getEntity());
        assertEquals(response.getStatus(), Response.Status.NOT_FOUND.getStatusCode());
    }
}