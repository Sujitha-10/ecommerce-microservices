package com.ecommerce.productservice.security;


import java.nio.charset.StandardCharsets;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;


@Service
public class JwtService {


    @Value("${jwt.secret}")
    private String secretKey;

    private SecretKey getSigningKey() {

        byte[] keyBytes =
                secretKey.getBytes(StandardCharsets.UTF_8);

        return Keys.hmacShaKeyFor(keyBytes);
    }



    public String extractUsername(String token) {

        return extractAllClaims(token)
                .getSubject();
    }



    public boolean isTokenValid(String token) {

        try {

            extractAllClaims(token);

            return true;

        } catch(Exception e) {

            return false;
        }
    }



    private Claims extractAllClaims(String token) {


        return Jwts.parser()

                .verifyWith(getSigningKey())

                .build()

                .parseSignedClaims(token)

                .getPayload();

    }

}