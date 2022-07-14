package org.rzldev.quarkus;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class MovieRepository implements PanacheRepository<Movie> {

    public List<Movie> findByCountry(String country) {
        return list(
                "SELECT m FROM movies m " +
                        "WHERE m.country = ?1 " +
                        "ORDER BY m.title",
                country
        );
    }

    public Movie updateMovie(Long id, Movie newMovie) {
        Movie oldMovie = findByIdOptional(id).orElse(null);
        if (oldMovie == null)
            return null;

        oldMovie.setTitle(newMovie.getTitle());
        oldMovie.setDescription(newMovie.getDescription());
        oldMovie.setDirector(newMovie.getDirector());
        oldMovie.setCountry(newMovie.getCountry());
        return oldMovie;
    }

}
