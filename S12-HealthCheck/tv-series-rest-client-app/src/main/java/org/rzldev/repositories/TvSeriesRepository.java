package org.rzldev.repositories;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import org.rzldev.entity.TvSeriesEntity;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TvSeriesRepository implements PanacheRepository<TvSeriesEntity> {
}
