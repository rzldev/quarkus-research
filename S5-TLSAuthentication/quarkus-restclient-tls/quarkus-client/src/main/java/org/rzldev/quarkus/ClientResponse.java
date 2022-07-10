package org.rzldev.quarkus;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.eclipse.microprofile.rest.client.inject.RestClient;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;

@Path("/client")
public class ClientResponse {

    @Inject
    @RestClient
    TLSClient client;

    @ConfigProperty(name="url")
    URL url;

    @ConfigProperty(name="keyStore")
    String keyStoreFile;

    @ConfigProperty(name="keyStorePassword")
    String keyStorePassword;

    @ConfigProperty(name="trustStore")
    String trustStoreFile;

    @ConfigProperty(name="trustStorePassword")
    String trustStorePassword;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return client.call();
    }

    @GET
    @Path("/builder")
    @Produces(MediaType.TEXT_PLAIN)
    public String helloWithBuilder() throws KeyStoreException, CertificateException, IOException, NoSuchAlgorithmException {
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream inputStreamKeyStore = this.getClass()
                .getClassLoader()
                .getResourceAsStream(keyStoreFile);
        keyStore.load(inputStreamKeyStore, keyStorePassword.toCharArray());

        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        InputStream inputStreamTrustStore = this.getClass()
                .getClassLoader()
                .getResourceAsStream(trustStoreFile);
        trustStore.load(inputStreamTrustStore, trustStorePassword.toCharArray());

        TLSClient clientBuilder = RestClientBuilder.newBuilder()
                .baseUrl(url)
                .keyStore(keyStore, keyStorePassword)
                .trustStore(trustStore)
                .build(TLSClient.class);

        return clientBuilder.call();
    }
}
