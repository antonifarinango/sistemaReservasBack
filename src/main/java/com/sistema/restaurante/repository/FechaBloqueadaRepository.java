/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.repository;

import com.sistema.restaurante.entities.FechaBloqueada;
import java.time.LocalDate;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Anthony
 */
@Repository
public interface FechaBloqueadaRepository extends JpaRepository<FechaBloqueada, UUID>{
    
    boolean existsByFecha(LocalDate fecha);
    
}
