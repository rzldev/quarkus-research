package org.rzldev.quarkus;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JWTService {
    
    public String generateJWT() {
        Set<String> roles = new HashSet<>(Arrays.asList("admin", "user"));
        return Jwt.issuer("ecommerce-jwt")
                .subject("ecommerce-jwt")
                .groups(roles)
                .expiresAt(System.currentTimeMillis() + 14400)
                .sign();
    }
    
}
