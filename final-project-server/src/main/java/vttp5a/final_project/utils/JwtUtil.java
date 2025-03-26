package vttp5a.final_project.utils;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtUtil {
    
    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private long expiration;

    @SuppressWarnings("deprecation")
    public String generateToken(String userId) {
        return Jwts.builder()
            .setSubject(userId)
            .setIssuedAt(new Date())
            .setExpiration(new Date(System.currentTimeMillis() + expiration))
            .signWith(SignatureAlgorithm.HS256, secret)
            .compact();
    }

    @SuppressWarnings("deprecation")
    public String extractUserId(String token) {
        return Jwts.parser()
        .setSigningKey(secret)
        .build()
        .parseSignedClaims(token)
        .getPayload()
        .getSubject();
    }

    public boolean validateToken(String token, String userId) {
        return extractUserId(token).equals(userId) && !isTokenExpired(token);
    }

    @SuppressWarnings("deprecation")
    private boolean isTokenExpired(String token) {
        return Jwts.parser()
            .setSigningKey(secret)
            .build()
            .parseSignedClaims(token)
            .getPayload()
            .getExpiration()
            .before(new Date());
    }
}
