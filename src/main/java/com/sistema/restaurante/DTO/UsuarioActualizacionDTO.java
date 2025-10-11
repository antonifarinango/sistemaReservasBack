/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.DTO;

import com.sistema.restaurante.entities.Rol;
import java.util.UUID;
import lombok.Data;

/**
 *
 * @author Anthony
 */
@Data
public class UsuarioActualizacionDTO {
    private UUID id;
    private String nombre;
    private String email;
    private String telefono;
    private Rol rol; 
}
