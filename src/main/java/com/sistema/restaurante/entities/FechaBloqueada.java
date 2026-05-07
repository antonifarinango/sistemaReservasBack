/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import java.time.LocalDate;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Anthony
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Data
public class FechaBloqueada {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    //CAMBIAR LA VALIDACION DE LA FECHA A NO NULL, DA ERRORES
    private LocalDate fecha;
    private String motivo;
    
    @ManyToOne
    @JoinColumn(name="resturante_id")
    private Restaurante restaurante;
    
}
