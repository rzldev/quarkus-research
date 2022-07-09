package org.rzldev;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.rzldev.models.Episode;
import org.rzldev.models.TvSeries;
import org.rzldev.proxies.EpisodeProxy;
import org.rzldev.proxies.TvSeriesProxy;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/tv-series")
public class TvSeriesResource {

    @RestClient
    TvSeriesProxy tvSeriesProxy;

    @RestClient
    EpisodeProxy episodeProxy;

    ArrayList<TvSeries> tvSeries = new ArrayList<>();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getMovies(
            @QueryParam("title") String title
    ) {
        if (title == null)
            return Response.ok(tvSeries).build();

        TvSeries show = getTvSeries(title);
        List<Episode> episodes = getEpisodes(show.getId());

        if (show.getId() == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        show.setEpisodes(episodes);
        tvSeries.add(show);

        return Response.ok(show).build();
    }

    @Fallback(fallbackMethod="fallbackGetTvSeries")
    public TvSeries getTvSeries(String title) {
        return tvSeriesProxy.get(title);
    }

    @Fallback(fallbackMethod="fallbackGetEpisodes")
    public List<Episode> getEpisodes(Long id) {
        return episodeProxy.get(id);
    }

    private TvSeries fallbackGetTvSeries(String title) {
        TvSeries show = new TvSeries();
        return show;
    }

    private List<Episode> fallbackGetEpisodes(Long id) {
        return new ArrayList<>();
    }
}
