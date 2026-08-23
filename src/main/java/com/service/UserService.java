package com.service;

import com.dao.UserRequest;
import com.model.Tipo_User;
import com.model.User;
import com.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Optional<User> buscarPorEmail (String email){
        return userRepository.findByEmail(email);
    }

    public User cadastrar (UserRequest request){
        if (userRepository.findByEmail(request.getEmail()).isPresent()){
            throw new RuntimeException("Email já cadastrado!");
        }
        User user = new User();

        user.setNome(request.getNome());
        user.setEmail(request.getEmail());
        user.setSenha(request.getSenha());
        user.setTipoUser(Tipo_User.cliente);
        user.setDataCadastro(LocalDate.now());

        return userRepository.save(user);
    }
}
