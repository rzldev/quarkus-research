package com.github.rzldev.configs;

import io.smallrye.config.ConfigMapping;
import io.smallrye.config.WithDefault;

@ConfigMapping(prefix = "inmemory")
public interface InMemoryCatalogueConfig {

    String connectionUrl();

    String username();

    String password();

    String dialect();

    String driver();

    @WithDefault("author_cache")
    String authorCacheName();

    @WithDefault("book_cache")
    String bookCacheName();

}
