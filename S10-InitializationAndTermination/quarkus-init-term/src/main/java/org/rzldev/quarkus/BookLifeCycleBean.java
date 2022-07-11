package org.rzldev.quarkus;

import io.quarkus.runtime.ShutdownEvent;
import io.quarkus.runtime.StartupEvent;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.transaction.Transactional;

@ApplicationScoped
public class BookLifeCycleBean {

    @Inject
    BookRepository bookRepository;

    private Book book;

    private static final Logger LOGGER = Logger.getLogger(BookLifeCycleBean.class);

    @Transactional
    void onStart(@Observes StartupEvent event) {
        LOGGER.info("The quarkus application is starting...");

        book = new Book();
        book.setTitle("Harry Potter and the Deathly Hallows");
        book.setAuthor("J. K. Rowling");
        bookRepository.persist(book);

        if (bookRepository.isPersistent(book))
            LOGGER.info("The book has been stored with id " + book.getId());
        else
            LOGGER.error("Failed to stored the book with id " + book.getId());
    }

    @Transactional
    void onStop(@Observes ShutdownEvent event) {
        LOGGER.info("The quarkus application is stopping...");

        boolean deleted = bookRepository.deleteById(book.getId());

        if (deleted)
            LOGGER.info("The book has been deleted");
        else
            LOGGER.error("Failed to delete the book");
    }

}
