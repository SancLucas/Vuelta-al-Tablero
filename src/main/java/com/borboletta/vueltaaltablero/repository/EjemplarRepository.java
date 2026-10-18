package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.Ejemplar;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long>{

    /* Los ejemplares de un juego dado de baja, o de un dueño dado de baja, desaparecen de la
    lista pública sin tocarlos: si el juego o el usuario se reactivan, vuelven solos. */
    @Query("""
           select e from Ejemplar e
             join fetch e.juego j
             join fetch e.propietario p
           where e.disponibleAlquiler = true
             and e.fechaBaja is null
             and j.fechaBaja is null
             and p.fechaBaja is null
           order by j.titulo""")
    List<Ejemplar> findDisponiblesParaAlquilar();

    @Query("""
           select e from Ejemplar e
             join fetch e.juego j
             join fetch e.propietario p
           where lower(p.correo) = lower(:correo)
             and e.fechaBaja is null
             and j.fechaBaja is null
           order by j.titulo""")
    List<Ejemplar> findActivosDe(@Param("correo") String correo);

    /* Busca por id Y por dueño a la vez: el ejemplar de otra persona simplemente "no existe",
    así que nadie puede editar o quitar uno ajeno cambiando el id en la URL. */
    @Query("""
           select e from Ejemplar e
             join fetch e.juego j
             join fetch e.propietario p
           where e.id = :id
             and lower(p.correo) = lower(:correo)
             and e.fechaBaja is null
             and j.fechaBaja is null""")
    Optional<Ejemplar> findActivoDe(@Param("id") Long id, @Param("correo") String correo);
}
