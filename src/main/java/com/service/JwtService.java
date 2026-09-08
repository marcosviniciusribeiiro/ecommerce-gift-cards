package com.service;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Service
public class JwtService {
    private final SecretKey chave;


    public JwtService (@Value("${jwt.secret}") String secret){
        this.chave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
    }

    public String gerarToken(Integer id, String email, String tipoUser){
        return Jwts.builder()
                .subject(String.valueOf(id))
                .claim("email", email)
                .claim("tipoUser", tipoUser)
                .issuedAt(new Date())
                //1 hora de validade para o token
                .expiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(chave)
                .compact();

    }

    public boolean validarToken(String token){
        try {
            Jwts.parser().verifyWith(chave).build().parseSignedClaims(token);
            return true;
        } catch (Exception e){
            return false;
        }
    }
}