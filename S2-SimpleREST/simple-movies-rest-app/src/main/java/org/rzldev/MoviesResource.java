package org.rzldev;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Path("movies")
@Tag(name="Movies Resources", description="Movie REST APIs")
public class MoviesResource {

    private static List<Movie> movies = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(
            operationId="retrieveMovies",
            summary="Retrieve Movies.",
            description="Retrieve all movies data."
    )
    @APIResponse(
            responseCode="200",
            description="Data retrieved.",
            content=@Content(mediaType=MediaType.APPLICATION_JSON)
    )
    public Response retrieveMovies() {
        return Response.ok(movies).build();
    }

    @GET
    @Path("size")
    @Produces(MediaType.TEXT_PLAIN)
    @Operation(
            operationId="getMovieSize",
            summary="Get movie size.",
            description="Get movie data size."
    )
    @APIResponse(
            responseCode="200",
            description="Movies retrieved.",
            content=@Content(mediaType=MediaType.TEXT_PLAIN)
    )
    public Response getMovieSize() {
        return Response.ok(movies.size()).build();
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId="createMovie",
            summary="Create movie.",
            description="Create a new movie data."
    )
    @APIResponse(
            responseCode="201",
            description="Movie a created.",
            content=@Content(mediaType=MediaType.APPLICATION_JSON)
    )
    public Response createMovie(
            @RequestBody(
                    description="Movie to create",
                    required=true,
                    content=@Content(schema=@Schema(implementation=Movie.class))
            )
            Movie movie
    ) {
        movies.add(movie);
        return Response.status(Response.Status.CREATED).entity(movie).build();
    }

    @PUT
    @Path("{movieId}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(
            operationId="updateMovie",
            summary="Update a movie.",
            description="Update an existing movie data."
    )
    @APIResponse(
            responseCode="200",
            description="Movie updated.",
            content=@Content(mediaType=MediaType.APPLICATION_JSON)
    )
    public Response updateMovie(
            @Parameter(
                    description="Existing movie id.",
                    required=true
            )
            @PathParam("movieId") Long movieId,
            @Parameter(
                    description="New movie title.",
                    required=true
            )
            @QueryParam("title") String movieTitle
    ) {
        movies = movies.stream().map(movie -> {
            if (movie.getId().equals(movieId))
                movie.setTitle(movieTitle);
            return movie;
        }).collect(Collectors.toList());
        return Response.ok(movies).build();
    }

    @DELETE
    @Path("{movieId}")
    @Consumes(MediaType.TEXT_PLAIN)
    @Operation(
            operationId="deleteMovie",
            summary="Delete a movie",
            description="Delete an existing movie data."
    )
    @APIResponse(
            responseCode="204",
            description="Movie deleted.",
            content=@Content(mediaType=MediaType.TEXT_PLAIN)
    )
    public Response deleteMovie(
            @PathParam("movieId") Long movieId
    ) {
        Optional<Movie> movieExists = movies.stream().filter(movie -> movie.getId().equals(movieId))
                .findFirst();

        if (!movieExists.isPresent())
            return Response.status(Response.Status.NOT_FOUND).build();

        boolean removed = movies.remove(movieExists.get());
        if (removed)
            return Response.noContent().build();

        return Response.status(Response.Status.BAD_REQUEST).build();
    }
}
