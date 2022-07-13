package org.rzldev;

import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.metrics.MetricUnits;
import org.eclipse.microprofile.metrics.annotation.Counted;
import org.eclipse.microprofile.metrics.annotation.Gauge;
import org.eclipse.microprofile.metrics.annotation.Metered;
import org.eclipse.microprofile.metrics.annotation.Timed;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.rzldev.entity.TvSeriesEntity;
import org.rzldev.models.Episode;
import org.rzldev.models.TvSeries;
import org.rzldev.proxies.EpisodeProxy;
import org.rzldev.proxies.TvSeriesProxy;
import org.rzldev.repositories.TvSeriesRepository;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Path("/tv-series")
public class TvSeriesResource {

    private static Logger LOGGER = Logger.getLogger(TvSeriesResource.class);

    @ConfigProperty(name="default.title")
    String defaultTitle;

    @RestClient
    TvSeriesProxy tvSeriesProxy;

    @RestClient
    EpisodeProxy episodeProxy;

    @Inject
    TvSeriesRepository tvSeriesRepository;

    ArrayList<TvSeries> tvSeries = new ArrayList<>();

    @PostConstruct
    void setUp() {
        ConfigProvider.getConfig().getOptionalValue("default.message", String.class)
                .ifPresent(title -> defaultTitle = title);
    }

    @GET
    @Path("/fetch")
    @Transactional
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(
            name="countFetchTvSeries",
            description="Count how many times the fetchTvSeries has been invoked."
    )
    @Timed(
            name="timeFetchTvSeries",
            description="How long it takes to invoke the fetchTvSeries.",
            unit=MetricUnits.MILLISECONDS
    )
    @Metered(
            name="meteredFetchTvSeries",
            description="Measures throughput of fetchTvSeries method."
    )
    public Response fetchTvSeries(
            @QueryParam("title") String title
    ) {
        if (title == null)
            title = defaultTitle;

        TvSeries series = getTvSeries(title);

        LOGGER.info(series);

        TvSeriesEntity entity = new TvSeriesEntity();
        entity.setName(series.getName());
        entity.setSummary(series.getSummary());
        tvSeriesRepository.persist(entity);

        if (tvSeriesRepository.isPersistent(entity))
            return Response.created(URI.create("/movies/" + entity.getId()))
                    .entity(entity).build();

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

//    @GET
//    @Path("/{id}")
//    @Produces(MediaType.APPLICATION_JSON)
//    @Gauge(
//            name="gaugeGetTvSeriesById",
//            description="Time of getByIdTvSeries method.",
//            unit=MetricUnits.NONE
//    )
//    public Response getTvSeriesById(
//            @PathParam("id") Long id
//    ) {
//        Optional<TvSeriesEntity> show = tvSeriesRepository.findByIdOptional(id);
//
//        if (show.isPresent()) return Response.ok(show).build();
//
//        return Response.status(Response.Status.NOT_FOUND).build();
//    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Counted(
            name="countGetAll",
            description="Count how many times the getAll has been invoked."
    )
    @Timed(
            name="timeGetAll",
            description="How long it takes to invoke the fetchTvSeries.",
            unit=MetricUnits.MILLISECONDS
    )
    @Metered(
            name="meteredGetAll",
            description="Measures throughput of getAll method."
    )
    public Response getAll(
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
