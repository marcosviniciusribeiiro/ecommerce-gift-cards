package com.repository;

import com.model.TokenRecuperacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TokenRecuperacaoRepository
        extends JpaRepository<TokenRecuperacao, Integer> {

    Optional<TokenRecuperacao> findByToken(String token);
}
