package com.service;

import com.repository.TokenRecuperacaoRepository;
import com.repository.UsuarioRepository;
import org.springframework.stereotype.Service;

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
}
