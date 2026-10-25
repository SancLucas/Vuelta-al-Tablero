package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.CategoriaJuego;
import com.borboletta.vueltaaltablero.entity.Juego;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long>{

    /* @EntityGraph trae la ficha técnica en la misma consulta: evita una consulta extra por cada juego (problema N+1). */
    @EntityGraph(attributePaths = "fichaTecnica")
    List<Juego> findAllByFechaBajaIsNullOrderByTituloAsc();

    // Una página del catálogo: el orden va en el nombre del método, no en lo que pida el cliente.
    @EntityGraph(attributePaths = "fichaTecnica")
    Page<Juego> findAllByFechaBajaIsNullOrderByTituloAsc(Pageable pageable);
    
    @EntityGraph(attributePaths = "fichaTecnica")
    Page<Juego> findAllByCategoriaAndFechaBajaIsNullOrderByTituloAsc(CategoriaJuego categoria, Pageable pageable);

    @EntityGraph(attributePaths = "fichaTecnica")
    Optional<Juego> findByIdAndFechaBajaIsNull(Long id);

    /* El título es único en la base: a lo sumo hay un juego dado de baja con ese título. */
    @EntityGraph(attributePaths = "fichaTecnica")
    Optional<Juego> findByTituloIgnoreCaseAndFechaBajaIsNotNull(String titulo);

    boolean existsByTituloIgnoreCaseAndFechaBajaIsNull(String titulo);

    /* Para editar: ¿otro juego (activo o dado de baja) ya usa ese título? */
    boolean existsByTituloIgnoreCaseAndFechaBajaIsNullAndIdNot(String titulo, Long id);

    boolean existsByTituloIgnoreCaseAndFechaBajaIsNotNullAndIdNot(String titulo, Long id);
}
