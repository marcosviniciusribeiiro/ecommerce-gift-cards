package com.config;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Encoders;

public class GerarChaveJWT {
    public static void main(String[] args) {
        String chave = Encoders.BASE64.encode(
                Jwts.SIG.HS256.key().build().getEncoded()
        );

        System.out.println(chave);
    }
}
