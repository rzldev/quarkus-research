package org.rzldev.health;

import io.smallrye.health.checks.UrlHealthCheck;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;
import org.eclipse.microprofile.health.Liveness;

import javax.enterprise.context.ApplicationScoped;
import java.util.Optional;

@ApplicationScoped
public class TvSeriesProxyUrl {

    @Liveness
    HealthCheck url() {
        return new UrlHealthCheck("https://api.tvmaze.com/shows/1/episodes")
                .name("API check url");
    }

}
