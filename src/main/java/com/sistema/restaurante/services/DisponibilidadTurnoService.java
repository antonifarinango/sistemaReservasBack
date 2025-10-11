/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.services;

import com.sistema.restaurante.entities.DisponibilidadTurno;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Anthony
 */
public interface DisponibilidadTurnoService {
    
    //LISTA DE TURNOS
    List<DisponibilidadTurno> obtenerTurnos();
    
    //TURNO POR ID
    DisponibilidadTurno obtenerTurno(UUID id);
    
    //CREAR TURNO
    DisponibilidadTurno crearTurno(DisponibilidadTurno turno);
    
    //ACTUALIZAR TURNO
    List<DisponibilidadTurno> editarTurno(List<DisponibilidadTurno> listaTurnos);
    
    //ELIMINAR TURNO
    void eliminarTurno(UUID id);
    
    
}
