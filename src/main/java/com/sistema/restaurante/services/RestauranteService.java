/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.services;

import com.sistema.restaurante.entities.Restaurante;

/**
 *
 * @author Anthony
 */
public interface RestauranteService {
    
    //OBTENER RESTAURANTE
    Restaurante restaurantePorId(Long id);
    //EDITAR RESTAURANTE
    Restaurante editarRestaurante(Long idReserva, Restaurante reserva);
    
}
