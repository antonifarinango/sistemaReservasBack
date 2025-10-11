/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.DTO;

import com.sistema.restaurante.entities.Rol;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.Data;

/**
 *
 * @author Anthony
 */
@Data
public class UsuarioDTO {
    
    private UUID id;
    private String nombre;
    private String email;
    private String telefono;
    private Rol rol; 
    private List<Map<String, String>> reserva;
    
}
