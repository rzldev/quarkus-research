package com.github.rzldev.services;

import com.github.rzldev.configs.InMemoryCatalogueConfig;
import com.github.rzldev.schemas.*;
import io.quarkus.runtime.StartupEvent;
import org.apache.commons.io.IOUtils;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.commons.configuration.XMLStringConfiguration;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

@ApplicationScoped
public class DataLoader {

    private static final Logger LOGGER = Logger.getLogger(DataLoader.class);

    @Inject
    RemoteCacheManager cacheManager;

    @Inject
    InMemoryCatalogueConfig inMemoryCatalogueConfig;

    private int totalPerCache = 1000;

    void onStart(@Observes StartupEvent event) {
        long start = new Date().getTime();
        try {
            LOGGER.info("Creating caches");

            URL tableStoreCacheConfig = this.getClass().getClassLoader().getResource("META-INF/protobufs/tableStore.xml");
            String bookConfigTable = replaceDBConnectionConfiguration(tableStoreCacheConfig, "books", BookKey.class, "Book");
            String authorConfigTable = replaceDBConnectionConfiguration(tableStoreCacheConfig, "authors", AuthorKey.class, "Author");

            RemoteCache<String, Author> authorCache = cacheManager.administration().getOrCreateCache(inMemoryCatalogueConfig.authorCacheName(),
                    new XMLStringConfiguration(authorConfigTable));

            RemoteCache<String, Book> bookCache = cacheManager.administration().getOrCreateCache(inMemoryCatalogueConfig.bookCacheName(),
                    new XMLStringConfiguration(bookConfigTable));

            LOGGER.info("Cleaning up past caches");
            cleanupCaches(authorCache, bookCache);
            LOGGER.info("Loading caches");
            loadData(authorCache, bookCache);
        } catch (IOException e) {
            LOGGER.error("IOException", e);
        } finally {
            LOGGER.info("Finished in " + (new Date().getTime() - start) + " ms");
        }
    }

    private void loadData(RemoteCache<String, Author> authorCache, RemoteCache<String, Book> bookCache) {
        List<Author> authors = null;
        try {
            authors = generateAuthorList(authorCache);
            generateBookList(authors, bookCache);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateBookList(List<Author> authors, RemoteCache<String, Book> bookCache) throws InterruptedException {
//        ArrayList books = new ArrayList<Book>(5);
//
//        Set<Author> authors1 = new HashSet<>(1);
//        authors1.add(authors.get(new Random().nextInt(0, 1)));
//        Book book1 = new Book(1l, "The Unexpected Boring Journey", "Description of The Unexpected Boring Journey",
//                1990, authors1, BigDecimal.valueOf(3.99));
//        books.add(book1);
//
//        Set<Author> authors2 = new HashSet<>(1);
//        authors2.add(authors.get(new Random().nextInt(0, 1)));
//        Book book2 = new Book(2l, "Barrels", "Description of Barrels", 2003, authors2,
//                BigDecimal.valueOf(4.49));
//        books.add(book2);
//
//        Set<Author> authors3 = new HashSet<>(1);
//        authors3.add(authors.get(new Random().nextInt(0, 1)));
//        Book book3 = new Book(3l, "Place and Time", "Description of Place and Time", 1997, authors3,
//                BigDecimal.valueOf(9.99));
//        books.add(book3);
//
//        Set<Author> authors4 = new HashSet<>(1);
//        authors4.add(authors.get(new Random().nextInt(0, 1)));
//        Book book4 = new Book(4l, "It Begins With M and Ends With E", "Description of It Begins With M and Ends With E",
//                2004, authors4, BigDecimal.valueOf(1.29));
//        books.add(book4);
//
//        Set<Author> authors5 = new HashSet<>(2);
//        authors5.addAll(authors);
//        Book book5 = new Book(5l, "Together", "Description of Together", 2010, authors5,
//                BigDecimal.valueOf(12.99));
//        books.add(book5);

        for (int x = 1; x <= totalPerCache; x++) {
            Book b = new Book(x, "book " + x, "Description of book " + x, 2000,
                    authors.get(x - 1).getId(), "3.99");
            bookCache.put(String.valueOf(b.getId()), b);
            Thread.sleep(10);
            LOGGER.debug(b.getTitle());
        }
    }

    private List<Author> generateAuthorList(RemoteCache<String, Author> authorCache) throws InterruptedException {
//        ArrayList authors = new ArrayList<Author>(2);
//        Author author1 = new Author(1l, "Dr. Ben Billy Bob Jr.", "Bob");
//        Author author2 = new Author(2l, "Marcus DeScornio Lupid 3rd", "Lupid");
//        authors.add(author1);
//        authors.add(author2);

        ArrayList<Author> authors = new ArrayList<>(10000);
        for (int x = 1; x <= totalPerCache; x++) {
            Author a = new Author(x, "author " + x, "author " + x);
            authors.add(a);
            authorCache.put(String.valueOf(a.getId()), a);
            Thread.sleep(10);
            LOGGER.debug(a.getName());
        }
        return authors;
    }

    private void cleanupCaches(RemoteCache<String, Author> authors, RemoteCache<String, Book> books) {
        try {
            CompletableFuture.allOf(authors.clearAsync(), books.clearAsync()).get(10, TimeUnit.SECONDS);
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            LOGGER.error("Something went wrong while cleaning the data.", e);
        }
    }


    private String replaceDBConnectionConfiguration(URL cacheConfig, String table, Class<? extends MessageKey> key, String message) throws IOException {
        String config = IOUtils.toString(cacheConfig, StandardCharsets.UTF_8)
                .replace("TABLE_NAME", table)
                .replace("MESSAGE_KEY", key.getSimpleName())
                .replace("MESSAGE_NAME", message)
                .replace("CONNECTION_URL", inMemoryCatalogueConfig.connectionUrl())
                .replace("USERNAME", inMemoryCatalogueConfig.username())
                .replace("PASSWORD", inMemoryCatalogueConfig.password())
                .replace("DIALECT", inMemoryCatalogueConfig.dialect())
                .replace("DRIVER", inMemoryCatalogueConfig.driver());
        LOGGER.info(config);
        return config;
    }
}
