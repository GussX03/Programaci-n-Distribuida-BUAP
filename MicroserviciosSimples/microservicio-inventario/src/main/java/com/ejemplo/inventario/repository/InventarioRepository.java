package com.ejemplo.inventario.repository;

import com.ejemplo.inventario.model.Inventario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventarioRepository extends JpaRepository<Inventario, Long> {
    // Buscar inventario por nombre de producto
    Optional<Inventario> findByProducto(String producto);
}