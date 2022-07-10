package org.rzldev.quarkus;

import org.rzldev.quarkus.models.Movie;

import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.List;

@Path("/movies")
public class MovieResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAll() {
        List<Movie> movies = Movie.listAll();
        return Response.ok(movies).build();
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieById(
            @PathParam("id") Long id
    ) {
        return Movie.findByIdOptional(id)
                .map(movie -> Response.ok(movie).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @GET
    @Path("/country/{country}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMoviesByCountry(
            @PathParam("country") String country
    ) {
         List<Movie> movies = Movie.list(
                 "SELECT m FROM movies m WHERE m.country = ?1 ORDER BY m.title",
                 country);
         if (movies.size() > 0)
            return Response.ok(movies).build();

         return Response.status(Response.Status.NOT_FOUND).build();
    }

    @GET
    @Path("/title/{title}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovieByTitle(
            @PathParam("title") String title
    ) {
        return Movie.find("title", title)
                .singleResultOptional()
                .map(movie -> Response.ok(movie).build())
                .orElse(Response.status(Response.Status.NOT_FOUND).build());
    }

    @POST
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createMovie(Movie movie) {
        Movie.persist(movie);
        if (movie.isPersistent())
            return Response.created(URI.create("/movies" + movie.id)).entity(movie).build();

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateMovieById(
            @PathParam("id") Long id, Movie newMovie
    ) {
        Movie movie = Movie.findById(id);
        if (movie == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        movie.title = newMovie.title;
        movie.description = newMovie.description;
        movie.director = newMovie.director;
        movie.country = newMovie.country;

        return Response.ok(movie).build();
    }

    @DELETE
    @Path("/{id}")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteMovieById(
            @PathParam("id") Long id
    ) {
        Movie movie = Movie.findById(id);
        if (movie == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        movie.delete();
        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
