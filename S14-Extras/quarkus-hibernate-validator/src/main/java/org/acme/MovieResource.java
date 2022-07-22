package org.acme;

import javax.inject.Inject;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.Validator;
import javax.validation.groups.ConvertGroup;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Path("/movies")
@Produces(MediaType.APPLICATION_JSON)
public class MovieResource {

    @Inject
    Validator validator;

    @Inject
    MovieService movieService;

    private List<Movie> movies = new ArrayList<>();

    @GET
    public Response getMovies() {
        return Response.ok(movies).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovie(Movie movie) {
        Set<ConstraintViolation<Movie>> validate = validator.validate(movie);
        if (validate.isEmpty()) {
            movies.add(movie);
            return Response.created(URI.create("/movies/")).entity(movie).build();
        } else {
            String violations = validate.stream()
                    .map(violation -> violation.getMessage())
                    .collect(Collectors.joining(", "));
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(violations).build();
        }
     }

    @POST
    @Path("/valid")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovieValid(
            @Valid
            @ConvertGroup(to = ValidationMovieGroup.Post.class)
            Movie movie
    ) {
        movies.add(movie);
        return Response.created(URI.create("/movies/")).entity(movie).build();
    }

    @POST
    @Path("/service")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createMovieWithService(Movie movie) {
        try {
            Movie newMovie = movieService.validate(movie);
            movies.add(newMovie);
            return Response.created(URI.create("/movies/")).entity(movie).build();
        } catch (ConstraintViolationException e) {
            String violations = e.getConstraintViolations().stream()
                    .map(violation -> violation.getMessage())
                    .collect(Collectors.joining(", "));

            return Response.status(Response.Status.BAD_REQUEST).entity(violations).build();
        }
    }

}
