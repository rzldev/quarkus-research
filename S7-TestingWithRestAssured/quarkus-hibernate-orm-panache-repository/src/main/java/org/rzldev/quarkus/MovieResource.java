package org.rzldev.quarkus;

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

    @Inject
    MovieRepository movieRepository;

    @GET
    public Response getAll() {
        List<Movie> movies = movieRepository.listAll();
        return Response.ok(movies).build();
    }

    @GET
    @Path("/{id}")
    public Response getMovieById(
            @PathParam("id") Long id
    ) {
        return movieRepository.findByIdOptional(id)
                .map(movie -> Response.ok(movie).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/title/{title}")
    public Response getMovieByTitle(
            @PathParam("title") String title
    ) {
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
        List<Movie> movies = movieRepository.findByCountry(country);

        if (movies.size() < 1)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(movies).build();
    }

    @POST
    @Transactional
    public Response createMovie(Movie movie) {
        movieRepository.persist(movie);
        if (movieRepository.isPersistent(movie))
            return Response.created(URI.create("movies/" + movie.getId()))
                    .entity(movie).build();

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    public Response updateMovie(
            @PathParam("id") Long id, Movie newMovie
    ) {
        Movie movie = movieRepository.updateMovie(id, newMovie);
        if (movie == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(movie).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    public Response deleteMovie(
            @PathParam("id") Long id
    ) {
        boolean deleted = movieRepository.deleteById(id);
        if (!deleted)
            return Response.status(Response.Status.NOT_FOUND).build();
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
