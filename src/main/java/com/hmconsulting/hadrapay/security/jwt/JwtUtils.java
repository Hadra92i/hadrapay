package com.hmconsulting.hadrapay.security.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.security.Key;
import java.util.Date;

@Component
public class JwtUtils {

    @Value("${hadrapay.jwt-secret}")
    private String JWT_SECRET_KEY;

    @Value("${hadraoay.jwt-expiration}")
    private Long JWT_EXPIRATION;

    public String generateJWT(Authentication authentication){
       String username = authentication.getName();

       Date currentDate = new Date();

       Date expiredDate = new Date(currentDate.getTime() + JWT_EXPIRATION);

       String token = Jwts.builder()
               .subject(username)
               .issuedAt(new Date())
               .expiration(expiredDate)
               .signWith(key())
               .compact();

       return token;
    }

    private Key key(){
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(JWT_SECRET_KEY));
    }

    public String getUsername(String token){
        return Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parseSignedClaims(token)
                .getPayload()
                .getSubject();
    }

    public boolean validateJWT(String token){
        Jwts.parser()
                .verifyWith((SecretKey) key())
                .build()
                .parse(token);

        return true;
    }
}
