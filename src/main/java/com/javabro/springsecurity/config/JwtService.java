package com.javabro.springsecurity.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    private static final String SECRET_KEY = "4816c3a3f918e0b0e6b9605a70c69b61d9a86bdcd4f7dcc96a7e94dfc169d2a7";

    public <T> T extractClaim(String token, Function<Claims, T> claims) {
            Claims claims1 = extractAllClaims(token);
            return claims.apply(claims1);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }


    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts.builder().setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+300000))
                .signWith(signingKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public boolean isTokenValid(String jwtToken, UserDetails userDetails) {
        String username = extractUsername(jwtToken);
        if(username.equals(userDetails.getUsername()) && isTokenExpired(jwtToken) )
            return true;
        return false;
    }

    private boolean isTokenExpired(String jwtToken) {
        return extactExpiration(jwtToken).before(new Date(System.currentTimeMillis()));
    }

    private Date extactExpiration(String jwtToken) {
        return extractClaim(jwtToken, Claims::getExpiration);
    }


    public Claims extractAllClaims(String jwtToken) {
        Jwts.parserBuilder()
                .setSigningKey(signingKey())
                .build()
                .parseClaimsJws(jwtToken)
                .getBody()
                .get("username");
        return null;
    }

    private Key signingKey() {
        byte[] bytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(bytes);
    }
}