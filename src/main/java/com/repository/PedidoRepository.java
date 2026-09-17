package com.repository;

import com.model.Pedido;
import com.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository
        extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuario(Usuario usuario);
}
