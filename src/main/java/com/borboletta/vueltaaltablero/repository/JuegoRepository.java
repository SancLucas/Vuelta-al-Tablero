package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.Juego;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JuegoRepository extends JpaRepository<Juego, Long>{
    
}
