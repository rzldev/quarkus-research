package org.rzldev.quarkus.models;

import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.Uni;
import io.vertx.mutiny.pgclient.PgPool;
import io.vertx.mutiny.sqlclient.Row;
import io.vertx.mutiny.sqlclient.Tuple;

public class Movie {

    private Long id;
    private String title;

    public Movie() {
    }

    public Movie(String title) {
        this.title = title;
    }

    public Movie(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private static Movie from(Row row) {
        return new Movie(row.getLong("id"), row.getString("title"));
    }

    public static Multi<Movie> findAll(PgPool client) {
        return client.query("SELECT * FROM movies ORDER BY title DESC").execute()
                .onItem().transformToMulti(set -> Multi.createFrom().iterable(set))
                .onItem().transform(Movie::from);
    }

    public static Uni<Movie> findById(PgPool client, Long id) {
        return client
                .preparedQuery("SELECT * FROM movies WHERE id=$1")
                .execute(Tuple.of(id))
                .onItem().transform(m -> m.iterator().hasNext()
                        ? from(m.iterator().next()) : null);
    }

    public static Uni<Long> create(PgPool client, String title) {
        return client
                .preparedQuery("INSERT INTO movies (title) VALUES ($1) RETURNING id")
                .execute(Tuple.of(title))
                .onItem().transform(m -> m.iterator().next().getLong("id"));
    }

    public static Uni<Movie> update(PgPool client, Long id, Movie movie) {
        return client
                .preparedQuery("UPDATE movies SET title = $1 WHERE id = $2 RETURNING *")
                .execute(Tuple.of(movie.getTitle(), id))
                .onItem().transform(m -> m.iterator().hasNext()
                        ? from(m.iterator().next()) : null);
    }

    public static Uni<Boolean> delete(PgPool client, Long id) {
        return client
                .preparedQuery("DELETE FROM movies where id=$1")
                .execute(Tuple.of(id))
                .onItem().transform(m -> m.rowCount() == 1);
    }
}
