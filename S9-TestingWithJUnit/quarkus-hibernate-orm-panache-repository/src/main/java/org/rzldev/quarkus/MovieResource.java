package org.rzldev.quarkus;

import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/movies")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    private static Logger LOGGER = Logger.getLogger(MovieResource.class);

    @Inject
    MovieRepository movieRepository;

    @GET
    public Response getAll() {
        LOGGER.debug("Get all movies inside the database");
        List<Movie> movies = movieRepository.listAll();
        return Response.ok(movies).build();
    }

    @GET
    @Path("/{id}")
    public Response getMovieById(
            @PathParam("id") Long id
    ) {
        LOGGER.debug("Get the movie by id " + id);
        return movieRepository.findByIdOptional(id)
                .map(movie -> Response.ok(movie).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/title/{title}")
    public Response getMovieByTitle(
            @PathParam("title") String title
    ) {
        LOGGER.debug("Get the movie by the title " + title);
        return movieRepository.find("title", title)
                .singleResultOptional()
                .map(movie -> Response.ok(movie).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/country/{country}")
    public Response getMovieByCountry (
            @PathParam("country") String country
    ) {
        LOGGER.debug("Get all movies by country " + country);
        List<Movie> movies = movieRepository.findByCountry(country);

        if (movies.size() < 1)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(movies).build();
    }

    @POST
    @Transactional
    public Response createMovie(Movie movie) {
        LOGGER.debug("Crete a new movie inside the database");
        movieRepository.persist(movie);

        if (movieRepository.isPersistent(movie)) {
            LOGGER.info("The movie has been created with id " + movie.getId());
            return Response.created(URI.create("movies/" + movie.getId()))
                    .entity(movie).build();
        }

        LOGGER.error("Failed to create a movie");
        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateMovie(
            @PathParam("id") Long id, Movie newMovie
    ) {
        LOGGER.debug("Update an existing movie inside the database by id " + id);
        Movie movie = movieRepository.updateMovie(id, newMovie);

        if (movie == null) {
            LOGGER.error("Failed to update a movie with id " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LOGGER.info("The movie has been updated with id " + id);
        return Response.ok(movie).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteMovie(
            @PathParam("id") Long id
    ) {
        LOGGER.debug("Delete an existing movie inside the database by id " + id);
        boolean deleted = movieRepository.deleteById(id);

        if (!deleted) {
            LOGGER.error("Failed to delete a movie with id " + id);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        LOGGER.info("The movie has been deleted with id " + id);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
