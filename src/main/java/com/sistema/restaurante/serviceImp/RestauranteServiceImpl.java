/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.serviceImp;

import com.sistema.restaurante.entities.Restaurante;
import com.sistema.restaurante.repository.RestauranteRepository;
import com.sistema.restaurante.services.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Anthony
 */
@Service
public class RestauranteServiceImpl implements RestauranteService{
    
    @Autowired
    private RestauranteRepository restauranteRepository;
    
   
    @Override
    public Restaurante restaurantePorId(Long id) {
        
        Restaurante restaurante = restauranteRepository.findById(id).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));
        
        return  restaurante;
        
    }

    @Override
    public Restaurante editarRestaurante(Long idReserva, Restaurante restaurante) {
        
        Restaurante restauranteActualizado = restauranteRepository.findById(idReserva).orElseThrow(()-> new RuntimeException("Restaurante no encontrado"));
        
        restauranteActualizado.setDescripcion(restaurante.getDescripcion());
        restauranteActualizado.setDireccion(restaurante.getDireccion());
        restauranteActualizado.setEmail(restaurante.getEmail());
        restauranteActualizado.setNombre(restaurante.getNombre());
        restauranteActualizado.setTelefono(restaurante.getTelefono());
        
        return restauranteRepository.save(restauranteActualizado);
        
    }

    
    
}
