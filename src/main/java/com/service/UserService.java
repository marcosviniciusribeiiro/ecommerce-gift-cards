package com.service;

import com.dao.UserRequest;
import com.exception.EmailAlreadyExistsException;
import com.exception.InvalidCredentialsException;
import com.model.Tipo_User;
import com.model.User;
import com.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private  final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public Optional<User> buscarPorEmail (String email){
        return userRepository.findByEmail(email);
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

    public User login(String email, String senha){
        User user = userRepository.findByEmail(email).orElseThrow(() -> new InvalidCredentialsException("Email ou senha incorreta."));

        if (!passwordEncoder.matches(senha, user.getSenha())){
            throw new InvalidCredentialsException("Email ou senha incorreta.");
        }

        return user;
    }
}
