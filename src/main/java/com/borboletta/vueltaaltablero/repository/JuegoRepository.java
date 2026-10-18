package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.Juego;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long>{

    /* @EntityGraph trae la ficha técnica en la misma consulta: evita una consulta extra por cada juego (problema N+1). */
    @EntityGraph(attributePaths = "fichaTecnica")
    List<Juego> findAllByFechaBajaIsNullOrderByTituloAsc();

    @EntityGraph(attributePaths = "fichaTecnica")
    Optional<Juego> findByIdAndFechaBajaIsNull(Long id);

    // Un título puede repetirse solo si el otro juego ya fue dado de baja.
    boolean existsByTituloIgnoreCaseAndFechaBajaIsNull(String titulo);

    boolean existsByTituloIgnoreCaseAndFechaBajaIsNullAndIdNot(String titulo, Long id);
}
