package com.config;

import com.service.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)
            throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .sessionManagement(session ->
                        session.sessionCreationPolicy(
                                SessionCreationPolicy.STATELESS
                        )
                )

                .authorizeHttpRequests(auth -> auth

                        // Rotas públicas
                        .requestMatchers(
                                "/index.html",
                                "/produto.html",
                                "/login.html",
                                "/cadastrar.html",
                                "/recuperar-senha.html",
                                "/redefinir-senha.html",
                                "/perfil.html", //autorização necessária
                                "/atualizar-conta.html", //autorização necessária
                                "/meus-pedidos.html", //autorização necessária
                                "/pedido.html", //autorização necessária
                                "/admin-pedidos.html", //autorização necessária
                                "/admin-produtos.html", //autorização necessária
                                "/cadastrar-produto.html", //autorização adm necessária
                                "/atualizar-produto.html", //autorização adm necessária
                                "/js/**",
                                "/css/**",
                                "/api/usuarios/cadastro",
                                "/api/usuarios/login",
                                "/api/usuarios/recuperar-senha",
                                "/api/usuarios/redefinir-senha"
                        ).permitAll()

                        // Catálogo público
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/produtos/**"
                        ).permitAll()

                        // Códigos - somente administrador
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/codigos",
                                "/api/codigos/**"
                        ).hasRole("ADMINISTRADOR")

                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/codigos"
                        ).hasRole("ADMINISTRADOR")

                        // Cadastro de produto - somente administrador
                        .requestMatchers(
                                HttpMethod.POST,
                                "/api/produtos/cadastro"
                        ).hasRole("ADMINISTRADOR")

                        // Atualização de produto - somente administrador
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/produtos/**"
                        ).hasRole("ADMINISTRADOR")

                        // Confirmação de pedido - somente administrador
                        .requestMatchers(
                                HttpMethod.PUT,
                                "/api/pedidos/*/confirmar"
                        ).hasRole("ADMINISTRADOR")

                        // Exclusão de produtos - somente administrador
                        .requestMatchers(
                                HttpMethod.DELETE,
                                "/api/produtos/{id}"
                        ).hasRole("ADMINISTRADOR")

                        // Consultar todos os pedidos - somente adiministrador
                        .requestMatchers(
                                HttpMethod.GET,
                                "/api/pedidos/all"
                        ).hasRole("ADMINISTRADOR")

                        // Todo o restante exige autenticação
                        .anyRequest().authenticated()
                )
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class
                );

        return http.build();
    }
}