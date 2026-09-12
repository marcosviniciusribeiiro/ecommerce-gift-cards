package com.service;

import com.dto.LoginResponse;
import com.dto.UserRequest;
import com.exception.EmailAlreadyExistsException;
import com.exception.InvalidCredentialsException;
import com.model.TipoUser;
import com.model.Usuario;
import com.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository repository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository repository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Optional<Usuario> buscarPorEmail (String email){
        return repository
                .findByEmail(email);
    }

    public Usuario cadastrar (UserRequest request){
        if (repository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("E-mail já cadastrado.");
        }
        Usuario usuario = new Usuario();
        usuario.setNome(request.getNome());
        usuario.setEmail(request.getEmail());

        //metodo para encriptar a senha do usuário antes de cadastrá-lo
        usuario.setSenha(passwordEncoder.encode(request.getSenha()));
        usuario.setTipoUser(TipoUser.cliente);
        usuario.setDataCadastro(LocalDate.now());

        return repository.save(usuario);
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
                usuario.getTipoUser().name()
        );

        return new LoginResponse(
                usuario.getId(),
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getTipoUser(),
                token
        );
    }
}
