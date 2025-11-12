package com.e_commerce_backend.e_commerce_backend.Utility;

import com.e_commerce_backend.e_commerce_backend.entity.RoleEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class JwtUtil {

    private final String secretKey = "jdbs4u95flksdfds;fk;lkdfkf;dls;lk;lk;k;k;koofdkgerog";
    private final long expirationMillis = 60 * 60 * 60 * 60; // ~10 hours

    /** ✅ Returns the cryptographic signing key. */
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(secretKey.getBytes(StandardCharsets.UTF_8));
    }

    /** ✅ Generate JWT token containing username + roles. */
    public String generateToken(String username, List<RoleEntity> roles) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expirationMillis);

        // Extract role names
        List<String> roleNames = roles.stream()
                .map(RoleEntity::getRole)
                .collect(Collectors.toList());

        // Custom claims
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", roleNames);

        // ✅ Order matters: set claims FIRST, then subject
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /** ✅ Validate token (check username and expiration). */
    public boolean validateToken(String token, String username) {
        String extractedUsername = extractUsername(token);
        return (extractedUsername.equals(username) && !isTokenExpired(token));
    }

    /** ✅ Extract username (subject). */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /** ✅ Extract roles. */
    public List<String> extractRoles(String token) {
        Claims claims = extractAllClaims(token);
        return claims.get("roles", List.class);
    }

    /** ✅ Extract any claim using a function. */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /** ✅ Check expiration. */
    private boolean isTokenExpired(String token) {
        Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    /** ✅ Parse all claims. */
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
