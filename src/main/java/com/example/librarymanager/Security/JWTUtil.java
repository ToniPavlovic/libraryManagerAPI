package com.example.librarymanager.Security;


import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;


@Component
public class JWTUtil {

    private static final long EXPIRATION_TIME = 60 * 60 * 1000; // 1 hour in milliseconds

    @Value("${jwt.secret}")
    private String secret;

    public String generateToken(String username, boolean isAdmin) throws
            IllegalArgumentException, JWTCreationException {
        return JWT.create()
                .withSubject("User Details")
                .withClaim("username", username)
                .withClaim("admin", isAdmin)
                .withIssuedAt(new Date())
                .withExpiresAt(new java.util.Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .sign(Algorithm.HMAC256(secret));
    }

    public DecodedJWT validateToken(String token) throws
            JWTVerificationException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(secret))
                .withSubject("User Details")
                .build();

        return verifier.verify(token);
    }

    // Helper to get username claim
    public String getUsernameFromToken(String token) {
        return validateToken(token).getClaim("username").asString();
    }

    // Helper to get admin claim
    public boolean isAdminFromToken(String token) {
        return validateToken(token).getClaim("admin").asBoolean();
    }
}
