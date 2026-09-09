package com.service;

import com.dao.LoginResponse;
import com.dao.UserRequest;
import com.exception.EmailAlreadyExistsException;
import com.exception.InvalidCredentialsException;
import com.model.Tipo_User;
import com.model.User;
import com.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.yaml.snakeyaml.tokens.KeyToken;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public Optional<User> buscarPorEmail (String email){
        return userRepository
                .findByEmail(email);
    }

    public User cadastrar (UserRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new EmailAlreadyExistsException("E-mail já cadastrado.");
        }
        User user = new User();
        user.setNome(request.getNome());
        user.setEmail(request.getEmail());

        //metodo para encriptar a senha do usuário antes de cadastrá-lo
        user.setSenha(passwordEncoder.encode(request.getSenha()));
        user.setTipoUser(Tipo_User.cliente);
        user.setDataCadastro(LocalDate.now());

        return userRepository.save(user);
    }

    public LoginResponse login(String email,
                               String senha){
        User user = userRepository
                .findByEmail(email)
                .orElseThrow(
                () -> new InvalidCredentialsException("Email ou senha incorreta."));

        if (!passwordEncoder.matches(senha, user.getSenha())){
            throw new InvalidCredentialsException("Email ou senha incorreta.");
        }

        String token = jwtService.gerarToken(
                user.getId(),
                user.getEmail(),
                user.getTipoUser().name()
        );

        return new LoginResponse(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getTipoUser(),
                token
        );
    }
}
