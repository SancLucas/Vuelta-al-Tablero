package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.CriticaJuego;
import com.borboletta.vueltaaltablero.entity.CriticaJuegoId;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CriticaJuegoRepository extends JpaRepository<CriticaJuego, CriticaJuegoId>{

    /* Críticas de un juego, de mejor a peor puntuación. */
    @Query("""
           select c from CriticaJuego c
             join fetch c.autor
           where c.juego.id = :idJuego
           order by c.opinion.puntuacion desc""")
    List<CriticaJuego> findDeJuego(@Param("idJuego") Long idJuego);

    /* La crítica que esta persona ya dejó sobre este juego, para precargar el formulario. */
    @Query("""
           select c from CriticaJuego c
             join fetch c.autor a
           where c.juego.id = :idJuego
             and lower(a.correo) = lower(:correo)""")
    Optional<CriticaJuego> findDeAutor(@Param("idJuego") Long idJuego,
                                       @Param("correo") String correo);
}