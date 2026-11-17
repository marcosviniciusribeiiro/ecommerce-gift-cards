package com.repository;

import com.model.TokenRecuperacao;
import com.model.Usuario;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface TokenRecuperacaoRepository
        extends JpaRepository<TokenRecuperacao, Integer> {

    Optional<TokenRecuperacao> findByToken(String token);

    void deleteByUsuario(Usuario usuario);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("""
    SELECT t
    FROM TokenRecuperacao t
    WHERE t.token = :token
    """)
    Optional<TokenRecuperacao> buscarPorTokenComBloqueio(
            @Param("token") String token
    );
}
