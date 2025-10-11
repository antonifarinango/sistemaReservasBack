/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.services;

import com.sistema.restaurante.entities.FechaBloqueada;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Anthony
 */
public interface FechaBloqueadaService {
    
    //LISTA DE TURNOS
    List<FechaBloqueada> obtenerFechasBloqueadas();
    
    //TURNO POR ID
    FechaBloqueada obtenerFechaBloqueada(UUID id);
    
    //CREAR TURNO
    FechaBloqueada crearFechaBloqueada (FechaBloqueada fecha);
    
    //ACTUALIZAR TURNO
    FechaBloqueada editarFechaBloqueada( UUID id, FechaBloqueada fecha);
    
    //ELIMINAR TURNO
    void eliminarFechaBloqueada(UUID id);
    
    //VERIFICAR SI LA FECHA ESTA BLOQUEADA
    boolean existenciaSegunFecha(LocalDate fecha);
    
}
