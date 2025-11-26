package com.streamLit.AuthService.authService.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.Map;
import java.security.Key;
import io.jsonwebtoken.io.Decoders;


@Service
public class JwtService {

    private final Key secretKey;

    public JwtService(@Value("${app.jwt.secret}") String secret) {
        this.secretKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    public enum TokenType{
        ACCESS , REFRESH ;
    }

    public String createToken(Map<String, Object> payload, TokenType type) {
        long now = System.currentTimeMillis();


        Date expDate ;
        if(type== TokenType.ACCESS){
            expDate = new Date(now+(1 * 24 * 60 * 60 * 1000));
        }
        else {
            expDate = new Date(now+(7 * 24 * 60 * 60 * 1000));
        }


        return Jwts.builder()
                .setClaims(payload)
                .setIssuedAt(new Date(now))
                .setExpiration(expDate)
                .signWith(secretKey)
                .compact();
    }


    public Claims verifyToken(String token) {

        try{
            return Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

        }catch(Exception e){
            return null;

        }

    }
}
