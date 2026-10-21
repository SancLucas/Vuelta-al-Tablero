package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.Alquiler;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AlquilerRepository extends JpaRepository<Alquiler, Long>{

    // Regla "un solo alquiler abierto por ejemplar": el servicio la consulta antes de registrar.
    boolean existsByEjemplarIdAndFechaDevolucionIsNull(Long idEjemplar);

    // Historial de una copia, del más reciente al más antiguo. El join fetch evita el N+1.
    @Query("""
           select a from Alquiler a
             join fetch a.inquilino
           where a.ejemplar.id = :idEjemplar
           order by a.fechaEntrega desc, a.id desc""")
    List<Alquiler> findHistorialDeEjemplar(@Param("idEjemplar") Long idEjemplar);

    // Alquileres "a nivel de juego": los de todas sus copias juntas.
    @Query("""
           select a from Alquiler a
             join fetch a.inquilino
             join fetch a.ejemplar e
           where e.juego.id = :idJuego
           order by a.fechaEntrega desc, a.id desc""")
    List<Alquiler> findHistorialDeJuego(@Param("idJuego") Long idJuego);

    // Base de la regla "solo critica quien alquiló y devolvió" (la usa el Paso 2).
    @Query("""
           select count(a) > 0 from Alquiler a
           where a.inquilino.nro = :nroUsuario
             and a.ejemplar.id = :idEjemplar
             and a.fechaDevolucion is not null""")
    boolean haDevueltoElEjemplar(@Param("nroUsuario") Long nroUsuario,
                                 @Param("idEjemplar") Long idEjemplar);
    
        // Lo que alquilé yo (como inquilino), lo más reciente primero.
    @Query("""
           select a from Alquiler a
             join fetch a.inquilino i
             join fetch a.ejemplar e
             join fetch e.juego
             join fetch e.propietario
           where lower(i.correo) = lower(:correo)
           order by a.fechaEntrega desc, a.id desc""")
    List<Alquiler> findDeInquilino(@Param("correo") String correo);

    // Los alquileres de MIS ejemplares (como propietario): desde acá registro las devoluciones.
    @Query("""
           select a from Alquiler a
             join fetch a.inquilino
             join fetch a.ejemplar e
             join fetch e.juego
             join fetch e.propietario p
           where lower(p.correo) = lower(:correo)
           order by a.fechaEntrega desc, a.id desc""")
    List<Alquiler> findDeEjemplaresDe(@Param("correo") String correo);

    /* Por id Y por propietario a la vez, igual que EjemplarRepository.findActivoDe: el alquiler
    de un ejemplar ajeno simplemente "no existe". */
    @Query("""
           select a from Alquiler a
             join fetch a.inquilino
             join fetch a.ejemplar e
             join fetch e.juego
             join fetch e.propietario p
           where a.id = :id
             and lower(p.correo) = lower(:correo)""")
    Optional<Alquiler> findDelPropietario(@Param("id") Long id, @Param("correo") String correo);
}