/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.DTO;

import com.sistema.restaurante.entities.Estado;
import com.sistema.restaurante.entities.EstadoAhora;
import java.util.List;
import java.util.UUID;
import lombok.Data;

/**
 *
 * @author Anthony
 */
@Data
public class MesaDTO {
    
    private UUID id;
    private int numero;
    private int capacidad;
    private EstadoAhora estadoActual;
    private Estado estado;
    private List<ReservaDTO> reservas;
    
}
