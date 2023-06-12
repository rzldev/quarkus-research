package org.acme;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

public class Movie {

    @NotBlank(message="The title must not be blank or empty.")
    @Length(min = 10, groups = ValidationMovieGroup.Post.class)
    @Length(min = 20, groups = ValidationMovieGroup.PostWithService.class)
    private String title;

    @NotBlank(message="The director must not be blank or empty.")
    private String director;

    @Min(message="The movie must be at least 60 minutes long.", value=40)
    private int minutes;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }
}
