package org.rzldev.quarkus;

import org.eclipse.microprofile.rest.client.inject.RegisterRestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/message")
@RegisterRestClient
public interface MessageClient {

    @GET
    String get();

}
