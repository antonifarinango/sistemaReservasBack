/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.repository;

import com.sistema.restaurante.entities.EstadoReserva;
import com.sistema.restaurante.entities.Reserva;
import java.time.LocalDateTime;
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
public interface ReservaRepository extends JpaRepository<Reserva, UUID> {

// SELECCIONAR LAS RESERVAS PARA HOY
    @Query("SELECT r "
            + "FROM Reserva r "
            + "WHERE r.estadoReserva = :estado "
            + "AND r.fecha BETWEEN :inicio AND :fin "
            + "ORDER BY r.fecha ASC")
    List<Reserva> findReservasHoy(
            @Param("estado") EstadoReserva estado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

// SELECCIONAR LAS RESERVAS SEGUN EL ESTADO
    @Query("SELECT r "
            + "FROM Reserva r "
            + "WHERE r.estadoReserva = :estado")
    List<Reserva> findByEstado(@Param("estado") EstadoReserva estado);

    // SELECCIONAR LAS RESERVAS CONFIRMADAS EN LA PRÓXIMA SEMANA (EXCLUYENDO HOY)
    @Query("SELECT r "
            + "FROM Reserva r "
            + "WHERE r.estadoReserva = :estado "
            + "AND r.fecha BETWEEN :inicio AND :fin "
            + "ORDER BY r.fecha ASC")
    List<Reserva> findProximasReservas(
            @Param("estado") EstadoReserva estado,
            @Param("inicio") LocalDateTime inicio,
            @Param("fin") LocalDateTime fin);

// HISTORIAL DE RESERVAS POR CLIENTE
    @Query("SELECT r "
            + "FROM Reserva r "
            + "JOIN FETCH r.mesa m "
            + "WHERE r.usuario.id = :idCliente "
            + "ORDER BY r.fecha DESC")
    List<Reserva> findHistorialByCliente(
            @Param("idCliente") UUID idCliente);

}
