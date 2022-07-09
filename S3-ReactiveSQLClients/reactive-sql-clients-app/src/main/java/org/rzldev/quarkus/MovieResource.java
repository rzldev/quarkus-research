package org.rzldev.quarkus;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import org.rzldev.quarkus.models.Movie;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.net.URI;

@Path("/movies")
public class MovieResource {

    @Inject
    PgPool pgCLient;

    @PostConstruct
    private void config() {
        initdb();
    }

    private void initdb() {
        pgCLient.query("DROP TABLE IF EXISTS movies").execute()
                .flatMap(m -> pgCLient.query(
                        "CREATE TABLE movies (id SERIAL PRIMARY KEY, title VARCHAR(100) NOT NULL)"
                ).execute())
                .flatMap(m -> pgCLient.query(
                        "INSERT INTO movies (title) VALUES ('Garfield')"
                ).execute())
                .flatMap(m -> pgCLient.query(
                        "INSERT INTO movies (title) VALUES ('Harry Potter')"
                ).execute())
                .await().indefinitely();
    }

    @GET
    public Multi<Movie> getAll() {
        return Movie.findAll(pgCLient);
    }

    @GET
    @Path("/{id}")
    public Uni<Response> getDetail(
            @PathParam("id") Long id
    ) {
        return Movie.findById(pgCLient, id)
                .onItem().transform(m -> m != null
                        ? Response.ok(m)
                        : Response.status(Response.Status.NOT_FOUND))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @POST
    public Uni<Response> save(Movie movie) {
        return Movie.create(pgCLient, movie.getTitle())
                .onItem().transform(id -> URI.create("/movies/" + id))
                .onItem().transform(uri ->Response.created(uri).build());
    }

    @PUT
    @Path("/{id}")
    public Uni<Response> update(
            @PathParam("id") Long id, Movie movie
    ) {
        return Movie.update(pgCLient, id, movie)
                .onItem().transform(m -> m != null
                        ? Response.ok(m)
                        : Response.status(Response.Status.NOT_FOUND))
                .onItem().transform(Response.ResponseBuilder::build);
    }

    @DELETE
    @Path("/{id}")
    public Uni<Response> delete(
            @PathParam("id") Long id
    ) {
        return Movie.delete(pgCLient, id)
                .onItem().transform(success -> success
                        ? Response.status(Response.Status.NO_CONTENT).build()
                        : Response.status(Response.Status.NOT_FOUND).build());
    }
}
