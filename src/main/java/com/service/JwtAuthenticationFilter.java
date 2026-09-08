package com.service;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jspecify.annotations.NonNull;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void doFilterInternal(HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain) throws ServletException, IOException {
        String autorizationHeader = request.getHeader("Authorization");

        if(autorizationHeader == null || !autorizationHeader.endsWith("Bearer")){
            filterChain.doFilter(request, response);
        }

        String token = autorizationHeader.substring(7);

        if(jwtService.validarToken(token)){
            System.out.println("Token válido!");
        } else {
            System.out.println("Token inválido!");
        }
        filterChain.doFilter(request, response);
    }
}
