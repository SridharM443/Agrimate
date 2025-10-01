
package com.example.agrimate.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.function.Function;

@Service
public class JwtService {

    private final Key key;
    private final long expirationMillis;

    public JwtService(
            @Value("${app.jwt.secret:change-me-please-change-me-please-change-me}") String secret,
            @Value("${app.jwt.expiration-ms:86400000}") long expirationMillis) {
        // Make sure the key has enough length for HS256 (min 256 bits)
        if (secret.length() < 32) {
            // pad to 32 chars
            secret = String.format("%-32s", secret).replace(' ', 'x');
        }
        this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expirationMillis = expirationMillis;
    }

    public String generateToken(String username) {
        Date now = new Date();
        Date exp = new Date(now.getTime() + expirationMillis);
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(exp)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public <T> T extractClaim(String token, Function<Claims, T> resolver) {
        Claims claims = getParser().parseClaimsJws(token).getBody();
        return resolver.apply(claims);
    }

    public boolean isValid(String token) {
        try {
            getParser().parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private JwtParser getParser() {
        return Jwts.parserBuilder().setSigningKey(key).build();
    }
}
