package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.CriticaEjemplar;
import com.borboletta.vueltaaltablero.entity.CriticaEjemplarId;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CriticaEjemplarRepository
        extends JpaRepository<CriticaEjemplar, CriticaEjemplarId>{

    // Críticas de una copia, de mejor a peor puntuación.
    @Query("""
           select c from CriticaEjemplar c
             join fetch c.autor
           where c.ejemplar.id = :idEjemplar
           order by c.opinion.puntuacion desc""")
    List<CriticaEjemplar> findDeEjemplar(@Param("idEjemplar") Long idEjemplar);
}