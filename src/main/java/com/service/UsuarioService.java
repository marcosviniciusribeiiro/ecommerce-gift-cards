package com.service;

import com.dto.LoginResponse;
import com.dto.UsuarioRequest;
import com.exception.EmailAlreadyExistsException;
import com.exception.InvalidCredentialsException;
import com.exception.UserNotFoundException;
import com.exception.UsuarioComPedidosException;
import com.model.TipoUsuario;
import com.model.Usuario;
import com.repository.PedidoRepository;
import com.repository.TokenRecuperacaoRepository;
import com.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PedidoRepository pedidoRepository;
    private final TokenRecuperacaoRepository tokenRecuperacaoRepository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(
            UsuarioRepository usuarioRepository,
            PedidoRepository pedidoRepository,
            TokenRecuperacaoRepository tokenRecuperacaoRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.pedidoRepository = pedidoRepository;
        this.tokenRecuperacaoRepository = tokenRecuperacaoRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Optional<Usuario> buscarPorEmail (String email){
        return usuarioRepository.findByEmail(email);
    }

    public Usuario cadastrar (UsuarioRequest request) {
        if (usuarioRepository.findByEmail(request.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Email já cadastrado.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        //metodo para encriptar a senha do usuário antes de cadastrá-lo
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTipoUsuario(TipoUsuario.Cliente);
        usuario.setDataCadastro(LocalDate.now());

        return usuarioRepository.save(usuario);
    }

    public Usuario atualizar(
            String emailUsuario,
            UsuarioRequest request) {
        Usuario usuario = usuarioRepository
                .findByEmail(emailUsuario)
                .orElseThrow(
                        () -> new UserNotFoundException("Usuário não encontrado.")
                );

        Optional<Usuario> usuarioComEmail = usuarioRepository.findByEmail(request.getEmail());

        if (usuarioComEmail.isPresent()
                &&
        !usuarioComEmail.get().getId().equals(usuario.getId())){
            throw new EmailAlreadyExistsException("Email já cadastrado.");
        }
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());
        usuario.setSenha(
                passwordEncoder.encode(request.getSenha())
        );

        return usuarioRepository.save(usuario);
    }

    @Transactional
    public void deletar(String emailUsuario){
        Usuario usuario = usuarioRepository
                .findByEmail(emailUsuario)
                .orElseThrow(
                        () -> new UserNotFoundException("Usuário não encontrado.")
                );

        if (pedidoRepository.existsByUsuario(usuario)) {
            throw new UsuarioComPedidosException("Não é possível excluir uma conta que possui pedidos registrados.");
        }

        tokenRecuperacaoRepository.deleteByUsuario(usuario);

        usuarioRepository.delete(usuario);
    }

    public LoginResponse login(
            String email,
            String senha) {
        Usuario usuario = usuarioRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new InvalidCredentialsException("Email ou senha incorreta.")
                );

        if (!passwordEncoder.matches(senha, usuario.getSenha())) {
            throw new InvalidCredentialsException("Email ou senha incorreta.");
        }

        String token = jwtService
                .gerarToken(
                usuario.getId(),
                usuario.getEmail(),
                usuario.getTipoUsuario().name()
                );

        return new LoginResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUsuario(),
                token
        );
    }

    public Usuario buscarUsuarioAutenticado(String email) {
        return usuarioRepository
                .findByEmail(email)
                .orElseThrow(
                        () -> new UserNotFoundException("Usuário não encontrado.")
                );
    }
}
