package org.example.backend.security;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class JWTService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    public String generateToken(String username) {
        final int millisInADay = 86400000;
        return Jwts.builder()
                .claims()
                .subject(username)
                .issuer("Alsayes")
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + millisInADay))
                .and()
                .signWith(genrateSecretKey())
                .compact();
    }

    private SecretKey genrateSecretKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String extractUsername(String jwt) {
        return Jwts.parser()
                .verifyWith(genrateSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getSubject();
    }

    public boolean isValidToken(String jwt, UserDetails userDetails) {
        String username = extractUsername(jwt);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(jwt);
    }

    private boolean isTokenExpired(String jwt) {
        return extractExpirationDate(jwt).before(new Date());
    }

    private Date extractExpirationDate(String jwt) {
        return Jwts.parser()
                .verifyWith(genrateSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload()
                .getExpiration();
    }
}
