package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.validation.Valid;
import javax.validation.groups.ConvertGroup;

@ApplicationScoped
public class MovieService {

    public Movie validate (
            @Valid
            @ConvertGroup(to = ValidationMovieGroup.PostWithService.class)
            Movie movie
    ) {
        return movie;
    }
}
