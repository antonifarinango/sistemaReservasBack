/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.services;

import com.sistema.restaurante.DTO.ReservaConMesaDTO;
import com.sistema.restaurante.DTO.ReservaDTO;
import com.sistema.restaurante.entities.Reserva;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Anthony
 */
public interface ReservaService {
    
    //LISTA DE RESERVAS
    List<ReservaConMesaDTO> obtenerReservas();
    
    //LISTA RESERVAS POR DIA
    List<ReservaConMesaDTO> obtenerReservasPorDia();
    
    //LISTA RESERVAS PENDIENTES
    List<ReservaConMesaDTO> obtenerReservasPendientes();
    
    //LISTA DE PROXIMAS RESERVAS
    List<ReservaConMesaDTO> obtenerReservasProximas();
    
    //HISTORIAL DE RESERVAS POR CLIENTE
    List<ReservaConMesaDTO> obtenerHistorialReservas(UUID idCliente);
    
    //OBTENER RESERVA POR ID
    Reserva obtenerReserva(UUID idReserva);
    
    //CREAR RESERVA 
    Reserva crearReserva(Reserva reserva);
    
    //EDITAR RESERVA
    Reserva editarReserva(UUID idReserva, Reserva reserva);
    
    //ELIMINAR RESERVA 
    void eliminarReserva(UUID idReserva);
    
}
