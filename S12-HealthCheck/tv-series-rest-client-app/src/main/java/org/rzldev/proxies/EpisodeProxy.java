package org.rzldev.proxies;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.rzldev.models.Episode;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/shows")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface EpisodeProxy {
//    http://api.tvmaze.com/shows/82/episodes

    @GET
    @Path("{id}/episodes")
    List<Episode> get(
            @PathParam("id") Long id
    );
}
