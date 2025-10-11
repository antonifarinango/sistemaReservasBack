/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import java.security.Key;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
/**
 *
 * @author Anthony
 */
@Component
public class JwtUtil {
   
    @Value("${jwt.secret}")
    private  String KEY;
    
    
    private Key getSignKey(){
        return Keys.hmacShaKeyFor(KEY.getBytes());
    }
    //GENERAR TOKEN
    public String generateToken(Authentication authentication){
        
        String username = authentication.getName();
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 *  60  * 60 * 10))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
        
    }
    
    //EXTRAER USERNAME
    public String extractUsername(String token){
          Claims claims = Jwts.parser()
               .setSigningKey(getSignKey())
               .build()
               .parseClaimsJws(token)
               .getBody();
          
          return claims.getSubject();
    }
    
    //VERIFICAR VALIDEZ DEL TOKEN
    private boolean isTokenExpired(String token){
        
        Date expiration = Jwts.parser()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration();
        
        return expiration.before(new Date());
        
    }
    
    //VALIDAR TOKEN
    public boolean validateToken(String token, UserDetails userDetails){
        
        String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
        
    }
    
}
