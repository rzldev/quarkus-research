package org.rzldev.health;

import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.rzldev.proxies.TvSeriesProxy;

import javax.inject.Inject;

@Liveness
public class TvSeriesProxyHealth implements HealthCheck {

    @Inject
    @RestClient
    TvSeriesProxy tvSeriesProxy;

    public HealthCheckResponse call() {
        tvSeriesProxy.get("title");
        return HealthCheckResponse.named("TvMaze APIs").up().build();
    }

}
