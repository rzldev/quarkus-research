package org.rzldev;

import org.eclipse.microprofile.openapi.annotations.OpenAPIDefinition;
import org.eclipse.microprofile.openapi.annotations.info.Info;
import org.eclipse.microprofile.openapi.annotations.info.License;
import org.eclipse.microprofile.openapi.annotations.tags.Tag;

import javax.ws.rs.core.Application;

@OpenAPIDefinition(
        info=@Info(
                title="Movie APIs",
                description="Movie REST APIs.",
                version="1.0.0",
                license=@License(
                        name="rzldev",
                        url="http://localhost:8080"
                )
        ),
        tags={
                @Tag(name="Movies", description="movies")
        }
)
public class MovieApplication extends Application {
}
