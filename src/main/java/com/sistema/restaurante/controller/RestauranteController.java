/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.controller;

import com.sistema.restaurante.entities.Restaurante;
import com.sistema.restaurante.services.RestauranteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Anthony
 */
@RestController
@RequestMapping("/api/v1/restaurantes")
@CrossOrigin(origins = "http://localhost:5173")
public class RestauranteController {
    
    @Autowired
    private RestauranteService restauranteService;
    
    @GetMapping("/restaurante-{id}")
    public Restaurante restaurantePorId(@PathVariable Long id){
        return restauranteService.restaurantePorId(id);
    }
    
    @PutMapping("/actualizar-{id}")
    public ResponseEntity<Restaurante> actualizarRestaurante (@PathVariable Long id, @RequestBody Restaurante restaurante){
        
        Restaurante restauranteNuevo = restauranteService.editarRestaurante(id, restaurante);
        
        return ResponseEntity.ok(restauranteNuevo);
        
    }
    
}
