package com.service;

import com.exception.TokenRecuperacaoInvalidoException;
import com.exception.UserNotFoundException;
import com.model.TokenRecuperacao;
import com.model.Usuario;
import com.repository.TokenRecuperacaoRepository;
import com.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenRecuperacaoService {
    private final TokenRecuperacaoRepository tokenRecuperacaoRepository;
    private final UsuarioRepository usuarioRepository;

    private final PasswordEncoder passwordEncoder;

    public TokenRecuperacaoService(
            TokenRecuperacaoRepository tokenRecuperacaoRepository,
            UsuarioRepository usuarioRepository,
            PasswordEncoder passwordEncoder) {
        this.tokenRecuperacaoRepository = tokenRecuperacaoRepository;
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
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

    public void redefinirSenha(
            String token,
            String novaSenha
    ){
        TokenRecuperacao tokenRecuperacao = tokenRecuperacaoRepository.findByToken(token)
                .orElseThrow(() ->
                        new TokenRecuperacaoInvalidoException("Token de recuperação inválido."));

        if(tokenRecuperacao.getUtilizado()){
            throw new TokenRecuperacaoInvalidoException("O token já foi utilizado.");
        }

        LocalDateTime dataExpiracao = tokenRecuperacao.getDataExpiracao();
        LocalDateTime dataAtual = LocalDateTime.now();

        if (dataAtual.isAfter(dataExpiracao)) {
            throw new TokenRecuperacaoInvalidoException("O token de recuperação expirou.");
        }

        Usuario usuario = tokenRecuperacao.getUsuario();
        usuario.setSenha(
                passwordEncoder.encode(novaSenha)
        );
        usuarioRepository.save(usuario);

        tokenRecuperacao.setUtilizado(true);
        tokenRecuperacaoRepository.save(tokenRecuperacao);
    }
}
