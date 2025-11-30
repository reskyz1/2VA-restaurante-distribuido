package org.sistema.repository;

import org.sistema.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    // Métodos CRUD básicos de Pedido são herdados.
}