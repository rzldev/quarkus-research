package com.github.rzldev.resources;

import com.github.rzldev.schemas.Author;
import io.quarkus.infinispan.client.Remote;
import org.infinispan.client.hotrod.RemoteCache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;

@ApplicationScoped
@Path("/infinispan")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class InfinispanResource {

    @Inject
    @Remote("local")
    RemoteCache<String, Author> cache;

    @GET
    @Path("{authorId}")
    public Response get(
            @PathParam("authorId") String id
    ) {
        Author author = cache.get(id);
        return Response.ok().entity(author).status(200).build();
    }

    @POST
    public Response create(Author author) {
        cache.put(String.valueOf(author.getId()), author);
        return Response.created(URI.create("author/" + author.getId())).entity(author).build();
    }

    @PUT
    public Response update(Author author) {
        cache.put(String.valueOf(author.getId()), author);
        return Response.ok().entity(author).build();
    }

    @DELETE
    @Path("{authorId}")
    public Response remove(
            @PathParam("authorId") Long id
    ) {
        cache.remove(id);
        return Response.ok().status(204).build();
    }

}
