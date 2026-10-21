package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.CriticaJuego;
import com.borboletta.vueltaaltablero.entity.CriticaJuegoId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CriticaJuegoRepository extends JpaRepository<CriticaJuego, CriticaJuegoId>{

    // Críticas de un juego, de mejor a peor puntuación.
    @Query("""
           select c from CriticaJuego c
             join fetch c.autor
           where c.juego.id = :idJuego
           order by c.opinion.puntuacion desc""")
    List<CriticaJuego> findDeJuego(@Param("idJuego") Long idJuego);
}