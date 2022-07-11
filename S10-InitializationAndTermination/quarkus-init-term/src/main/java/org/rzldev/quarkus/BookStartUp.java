package org.rzldev.quarkus;

import io.quarkus.runtime.Startup;
import org.jboss.logging.Logger;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.Optional;

@Startup
public class BookStartUp {

    private static Logger LOGGER = Logger.getLogger(BookStartUp.class);

    @Inject
    BookRepository bookRepository;

    @PostConstruct
    private void setUp() {
        Optional<Book> book = bookRepository
                .find("title", "Harry Potter and the Deathly Hallows")
                .singleResultOptional();

        if (book.isPresent())
            LOGGER.info(
                    "The book with title " + book.get().getTitle()
                    + " and " + book.get().getId() + " has been found"
            );
        else
            LOGGER.error("The book with that title cannot be found");
    }

}
