package com.service;

import com.dto.LoginResponse;
import com.dto.UsuarioRequest;
import com.exception.EmailAlreadyExistsException;
import com.exception.InvalidCredentialsException;
import com.exception.UserNotFoundException;
import com.model.TipoUsuario;
import com.model.Usuario;
import com.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UsuarioService {
    private final UsuarioRepository repository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UsuarioService(
            UsuarioRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }


    public Optional<Usuario> buscarPorEmail (String email){
        return repository.findByEmail(email);
    }

    public Usuario cadastrar (UsuarioRequest request){
        if (repository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("E-mail já cadastrado.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        //metodo para encriptar a senha do usuário antes de cadastrá-lo
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTipoUsuario(TipoUsuario.cliente);
        usuario.setDataCadastro(LocalDate.now());

        return repository.save(usuario);
    }

    public Usuario atualizar(String emailUsuario, UsuarioRequest request){
        Usuario usuario = repository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        Optional<Usuario> usuarioComEmail = repository.findByEmail(request.getEmail());

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

        return repository.save(usuario);
    }

    public void deletar(String emailUsuario){
        Usuario usuario = repository.findByEmail(emailUsuario)
                .orElseThrow(() -> new UserNotFoundException("Usuário não encontrado."));

        repository.delete(usuario);
    }


    public LoginResponse login(String email,
                               String senha){
        Usuario usuario = repository
                .findByEmail(email)
                .orElseThrow(
                () -> new InvalidCredentialsException("Email ou senha incorreta."));

        if (!passwordEncoder.matches(senha, usuario.getSenha())){
            throw new InvalidCredentialsException("Email ou senha incorreta.");
        }

        String token = jwtService.gerarToken(
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

    public Usuario buscarUsuarioAutenticado(String email){
        return repository.findByEmail(email)
                .orElseThrow(() ->
                        new UserNotFoundException("Usuário não encontrado."));
    }
}
