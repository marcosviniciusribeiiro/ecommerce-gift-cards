package com.repository;

import com.model.Pedido;
import com.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PedidoRepository
        extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuario(Usuario usuario);

    Optional<Pedido> findByIdAndUsuario(Integer id, Usuario usuario);
}
