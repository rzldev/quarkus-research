package org.rzldev.quarkus;

import io.vertx.core.json.JsonObject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
@Path("/api/jwt")
@Produces(MediaType.APPLICATION_JSON)
public class JWTResource {

    @Inject
    JWTService jwtService;

    @GET
    public Response getJwt() {
        String jwtToken = jwtService.generateJWT();
        JsonObject json = new JsonObject();
        json.put("token", jwtToken);
        return Response.ok(json).build();
    }

}
