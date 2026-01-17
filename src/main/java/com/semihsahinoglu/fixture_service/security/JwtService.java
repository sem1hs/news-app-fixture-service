package com.semihsahinoglu.fixture_service.security;

import com.semihsahinoglu.fixture_service.entity.JwtProperties;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private final JwtProperties jwtProperties;

    public JwtService(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public boolean validateToken(String token) {
        Date expirationDate = extractExpiration(token);
        return !expirationDate.before(new Date());
    }

    public List<GrantedAuthority> extractRoles(String token) {

        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        String scope = claims.get("scope", String.class);
        if (scope != null) {
            return List.of(new SimpleGrantedAuthority("SCOPE_" + scope));
        }

        List<String> roles = claims.get("roles", List.class);
        if (roles != null) {
            return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
        }

        return List.of();
    }

    public String extractUsername(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getSubject();
    }

    private Date extractExpiration(String token) {
        Claims claims = Jwts.parser()
                .verifyWith((SecretKey) getSignKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

        return claims.getExpiration();
    }

    private Key getSignKey() {
        String JWT_KEY = jwtProperties.getSecret();
        byte[] keyBytes = Decoders.BASE64.decode(JWT_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}