package org.rzldev.quarkus;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    BookRepository bookRepository;

    @GET
    @Transactional
    public Response getAllBooks() {
        List<Book> books = bookRepository.listAll();
        return Response.ok(books).build();
    }

}
