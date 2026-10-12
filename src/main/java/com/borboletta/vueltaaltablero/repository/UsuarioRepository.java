package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.Usuario;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository {
    Usuario save(Usuario usuario);
    Optional<Usuario> findById(Long id);
    List<Usuario> findAll();
    boolean deleteById(Long id);
    Usuario update(Usuario usuario);
}
