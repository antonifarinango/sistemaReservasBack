/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.services;

import com.sistema.restaurante.entities.HorarioRestaurante;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Anthony
 */
public interface HorarioRestauranteService {
     //LISTA DE TURNOS
    List<HorarioRestaurante> obtenerHorariosRestaurante();
    
    //TURNO POR ID
    HorarioRestaurante obtenerHorarioRestaurante(UUID id);
    
    //CREAR TURNO
    HorarioRestaurante crearHorarioRestaurante (HorarioRestaurante horario);
    
    //ACTUALIZAR TURNO
   List<HorarioRestaurante> editarHorarioRestaurante(List<HorarioRestaurante> horario);
    
    //ELIMINAR TURNO
    void eliminarHorarioRestaurante(UUID id);
    
    //HORARIO SEGUN EL DIA DE LA SEMANA
    HorarioRestaurante obtenerPorDia(String dia);
}
