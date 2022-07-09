package org.rzldev.quarkus;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import java.util.concurrent.TimeUnit;

@Path("/message")
public class MessageResource {

    @GET
    public String message() {
        try {
            TimeUnit.MILLISECONDS.sleep(400);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return "Hello from quarkus-service-message";
    }

}
