package com.service;

import com.exception.TokenRecuperacaoInvalidoException;
import com.exception.UserNotFoundException;
import com.model.TokenRecuperacao;
import com.model.Usuario;
import com.repository.TokenRecuperacaoRepository;
import com.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenRecuperacaoService {
    private final TokenRecuperacaoRepository tokenRecuperacaoRepository;
    private final UsuarioRepository usuarioRepository;

    public TokenRecuperacaoService(
            TokenRecuperacaoRepository tokenRecuperacaoRepository,
            UsuarioRepository usuarioRepository) {
        this.tokenRecuperacaoRepository = tokenRecuperacaoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public TokenRecuperacao gerarToken(String email) {
        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado."));

        TokenRecuperacao token = new TokenRecuperacao();
        token.setUsuario(usuario);
        token.setToken(
                UUID.randomUUID().toString()
        );
        token.setDataExpiracao(
                LocalDateTime.now().plusMinutes(15)
        );
        token.setUtilizado(false);

        return tokenRecuperacaoRepository.save(token);
    }

    public TokenRecuperacao validarToken(String token){
        TokenRecuperacao tokenRecuperacao = tokenRecuperacaoRepository.findByToken(token)
                .orElseThrow(() ->
                        new TokenRecuperacaoInvalidoException("Token não encontrado."));

        boolean utilizado = tokenRecuperacao.getUtilizado();

        if (utilizado){
            throw new TokenRecuperacaoInvalidoException("O token não pode ser utilizado.");
        }

    }
}
