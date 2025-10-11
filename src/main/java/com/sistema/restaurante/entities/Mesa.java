/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Anthony
 */
@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor

public class Mesa {

    @Id
    @GeneratedValue
    private UUID id;
    
    private int numero;
    
    private int capacidad;
    
    @Enumerated(EnumType.STRING)
    private EstadoAhora estadoActual; 
    
    
   @Enumerated(EnumType.STRING)
   @Column(length = 20)
    private Estado estado; // OCUPADO / DISPONIBLE
    
    @OneToMany(mappedBy = "mesa")
    private List<Reserva> reservas;
    
}
