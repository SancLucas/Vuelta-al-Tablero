package com.borboletta.vueltaaltablero.repository;

import com.borboletta.vueltaaltablero.entity.Ejemplar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EjemplarRepository extends JpaRepository<Ejemplar, Long>{
    
}
