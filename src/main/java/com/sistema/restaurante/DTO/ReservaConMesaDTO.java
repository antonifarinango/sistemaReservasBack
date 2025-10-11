/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.DTO;

import com.sistema.restaurante.entities.EstadoReserva;
import com.sistema.restaurante.entities.Turno;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.Data;

/**
 *
 * @author Anthony
 */

@Data
public class ReservaConMesaDTO {
    
    private UUID id;
    private LocalDateTime fecha;
    private Turno turno; 
    private int cantidadPersonas;
    private EstadoReserva estadoReserva;
    private UsuarioActualizacionDTO usuario;
    private MesaActualizacionDTO mesa;
    
}
