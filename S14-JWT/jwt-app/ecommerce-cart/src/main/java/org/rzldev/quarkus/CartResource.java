package org.rzldev.quarkus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Produces(MediaType.APPLICATION_JSON)
@Path("/api/cart")
public class CartResource {

    private List<CartItem> cartItems = new ArrayList<>(100);

    @GET
    @PermitAll
    public Response getItems() {
        return Response.ok(cartItems).build();
    }

    @POST
    @RolesAllowed({"admin", "user"})
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addItem(CartItem item) {
        item.setId((long) cartItems.size() + 1);
        cartItems.add(item);
        return Response.ok(item).build();
    }

    @DELETE
    @RolesAllowed({"admin", "user"})
    @Path("/{id}")
    public Response deleteItem(@PathParam("id") Long id) {
        Optional<CartItem> cartItemOpt = cartItems.stream().filter(item -> item.getId().equals(id)).findFirst();
        if (cartItemOpt.isPresent()) {
            cartItems.remove(cartItemOpt.get());
            return Response.noContent().build();
        }
        return Response.status(Response.Status.NOT_FOUND).build();
    }

}
