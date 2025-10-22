/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.repository;

import com.sistema.restaurante.entities.Mesa;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Anthony
 */
@Repository
public interface MesaRepository extends JpaRepository<Mesa, UUID> {

    /*@Query("SELECT COALESCE(MAX(m.numero),0) FROM Mesa m")
    int obtenerUltimaNumeroMesa(); */
    
    //BUSCAR MESAS DISPONIBLES
    @Query("""
    SELECT m
    FROM Mesa m
    WHERE m.activa = true
      AND m.id NOT IN (
          SELECT r.mesa.id
          FROM Reserva r
          WHERE r.fecha = :fechaCompleta
      )
    """)
List<Mesa> findMesasDisponibles(@Param("fechaCompleta") LocalDateTime fechaCompleta);

}
