package org.rzldev.quarkus;

import org.bson.types.ObjectId;

import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

@Path("/knights")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class KnightResource {

    @Inject
    KnightRepository knightRepository;

    @GET
    public Response getAll(
            @QueryParam("orderBy") String orderBy
    ) {
        List<Knight> knights = new ArrayList<>();

        if (orderBy != null) {
            if (orderBy.equals("name"))
                knights = knightRepository.findOrderName();
        } else {
            knights = knightRepository.listAll();
        }

        return Response.ok(knights).build();
    }

    @GET
    @Path("/{id}")
    public Response getById(
            @PathParam("id") String id
    ) {
        Knight knight = knightRepository.findById(new ObjectId(id));

        if (knight == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(knight).build();
    }

    @GET
    @Path("/name/{name}")
    public Response getByName(
            @PathParam("name") String name
    ) {
        Knight knight = knightRepository.findByName(name);

        if (knight == null)
            return Response.status(Response.Status.NOT_FOUND).build();

        return Response.ok(knight).build();
    }

    @POST
    public Response create(Knight knight) throws URISyntaxException {
        knightRepository.persist(knight);

        return Response
                .created(URI.create("/knights/" + knight.id))
                .entity(knight)
                .build();
    }

    @PUT
    @Path("/{id}")
    public Response update(
            @PathParam("id") String id, Knight knight
    ) {
        knight.id = new ObjectId(id);
        knightRepository.update(knight);
        return Response.ok(knight).build();
    }

    @DELETE
    @Path("/{id}")
    public Response delete(
            @PathParam("id") String id
    ) {
        Knight knight = knightRepository.findById(new ObjectId(id));
        knightRepository.delete(knight);

        return Response.status(Response.Status.NO_CONTENT).build();
    }

}
