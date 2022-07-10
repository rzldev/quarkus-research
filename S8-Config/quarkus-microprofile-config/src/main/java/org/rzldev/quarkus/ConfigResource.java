package org.rzldev.quarkus;

import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/config")
public class ConfigResource {

    @Inject
    @ConfigProperty(
            name="config.message.inject",
            defaultValue="Hello from ConfigResource (default value)"
    )
    String message;

    @Inject
    CustomConfig customConfig;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessageInject() {
        return message;
    }

    @GET
    @Path("/provider")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessageByProvider() {
        Config config = ConfigProvider.getConfig();
        return config.getValue("config.message.provider", String.class);
    }

    @GET
    @Path("/provider-optional")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessageByProviderOptional() {
        return ConfigProvider.getConfig()
                .getOptionalValue("config.message.provider", String.class)
                .orElse("i cannot find config.message.provider");
    }

    @GET
    @Path("/custom")
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessageByCustomConfig() {
        return customConfig.message;
    }

}
