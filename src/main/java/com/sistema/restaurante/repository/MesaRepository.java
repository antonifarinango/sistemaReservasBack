/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.repository;

import com.sistema.restaurante.entities.Mesa;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Anthony
 */
@Repository
public interface MesaRepository  extends JpaRepository<Mesa, UUID>{
    
    /*@Query("SELECT COALESCE(MAX(m.numero),0) FROM Mesa m")
    int obtenerUltimaNumeroMesa(); */
}
