/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.entities;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import java.time.LocalTime;
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
public class HorarioRestaurante {
    
    @Id
    @GeneratedValue
    private UUID id;
    
    private String diaSemana;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaApertura;
    @JsonFormat(pattern = "HH:mm")
    private LocalTime horaCierre;
    
}
