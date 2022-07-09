package org.rzldev.quarkus;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

@Path("/message")
public class MessageResource {

//    Use this command to run it on CLI
//    while true; do sleep 1; curl http://localhost:8080/message; echo -e '\n'; done

    @RestClient
    MessageClient client;

    @GET
    public String message() {
        return client.get();
    }

    @GET
    @Path("/circuit-breaker")
    @Timeout(200)
    @CircuitBreaker(
            requestVolumeThreshold=4,
            failureRatio=.5,
            delay=4000,
            successThreshold=3
    )
    @Fallback(fallbackMethod="getMessageFallback")
    public String messageWithCircuitBreaker() {
        return client.get();
    }

    @GET
    @Path("/max-retries")
    @Timeout(200)
    @Retry(maxRetries=3)
    @Fallback(fallbackMethod="getMessageFallback")
    public String messageWithMaxRetries() {
        return client.get();
    }

    private String getMessageFallback() {
        return "Get message fallback called";
    }

}
