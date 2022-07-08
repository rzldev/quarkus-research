package org.rzldev;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema(name="Movie model.", description="Movie model prototype.")
public class Movie {

    private Long id;

    @Schema(required=true)
    private String title;

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
}
