package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

    // Solo cuentas activas: ingreso, perfil y edición.
    Optional<Usuario> findByCorreoIgnoreCaseAndFechaBajaIsNull(String correo);
    List<Usuario> findAllByFechaBajaIsNull();

    // Solo cuentas DADAS DE BAJA: para reactivarlas cuando se registran de nuevo.
    Optional<Usuario> findByCorreoIgnoreCaseAndDniAndFechaBajaIsNotNull(String correo, String dni);

    // Sin filtro a propósito: un correo o DNI de cualquier cuenta (activa o no) está ocupado.
    boolean existsByCorreoIgnoreCase(String correo);
    boolean existsByDni(String dni);
}