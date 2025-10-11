/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.services;

import com.sistema.restaurante.DTO.MesaDTO;
import com.sistema.restaurante.DTO.MesaActualizacionDTO;
import com.sistema.restaurante.entities.Mesa;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Anthony
 */
public interface MesaService {
    
    //OBTENER MESAS
    List<MesaDTO> obtenerMesas();
    //OBTENER MESA POR ID
    Mesa obtenerMesaPorId(UUID idMesa);
    //CREAR MESA
    Mesa crearMesa(Mesa mesa);
    //EDITAR MESA
    MesaDTO editarMesa(UUID idMesa,MesaActualizacionDTO nuevaMesa);
    //ELIMINAR MESA
    void eliminarMesa(UUID idMesa);
    //VER MESAS DISPONIBLES
    boolean estaDisponible(UUID idMesa);
    
}
