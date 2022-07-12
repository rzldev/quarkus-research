package org.rzldev.proxies;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;
import org.rzldev.models.TvSeries;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

@Path("/singlesearch")
@Produces(MediaType.APPLICATION_JSON)
@RegisterRestClient
public interface TvSeriesProxy {
//    http://api.tvmaze.com/singlesearch/shows?q=garfield

    @GET
    @Path("/shows")
    TvSeries get(
            @QueryParam("q") String title
    );
}
