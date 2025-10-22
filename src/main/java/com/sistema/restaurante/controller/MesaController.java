/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.controller;

import com.sistema.restaurante.DTO.MesaDTO;
import com.sistema.restaurante.DTO.MesaActualizacionDTO;
import com.sistema.restaurante.entities.Mesa;
import com.sistema.restaurante.mappers.SistemaReservaMapper;
import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import com.sistema.restaurante.services.MesaService;
import java.time.LocalDateTime;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Anthony
 */
@RestController
@RequestMapping("/api/v1/mesas")
@CrossOrigin(origins = "http://localhost:5173") 
public class MesaController {
    
    @Autowired
    private MesaService mesaService;
    @Autowired
    private SistemaReservaMapper mapper;
    
    @GetMapping("/todos")
    public List<MesaDTO> listaMesas(){
        
        return mesaService.obtenerMesas();
        
    }
    
    @GetMapping("administracion/{id}")
    public ResponseEntity<MesaDTO> mesa(@PathVariable UUID id){
        
        try {
            
             Mesa  mesa = mesaService.obtenerMesaPorId(id);
             MesaDTO  mesaDTO = mapper.mappearMesa(mesa);
             
             return ResponseEntity.ok(mesaDTO);
             
        } catch (RuntimeException r) {
            
            return ResponseEntity.notFound().build();
           
        }
    }
    
    @PostMapping("administracion/crear")
    public ResponseEntity<Mesa> guardarMesa(@RequestBody Mesa mesa){
        
        Mesa nuevaMesa = mesaService.crearMesa(mesa);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevaMesa.getId())
                .toUri();

        return ResponseEntity.created(location).body(nuevaMesa);
        
    }
    
    @PutMapping("administracion/actualizar/{id}")
     public ResponseEntity<MesaDTO> updateMesa(@PathVariable UUID id, @RequestBody MesaActualizacionDTO dto) {

        MesaDTO response = mesaService.editarMesa(id, dto);
        return ResponseEntity.ok(response);
        
    }
    
    @DeleteMapping("administracion/eliminar/{id}")
   public ResponseEntity<Map<String,Boolean>> eliminarMesa(@PathVariable UUID id){
       
       mesaService.eliminarMesa(id);
       
       Map<String,Boolean> response = new HashMap<>();
       
       response.put("delete", Boolean.TRUE);
       
       return ResponseEntity.ok(response);
   }
   
   @GetMapping("/disponibles")
    public List<MesaActualizacionDTO> obtenerMesasDisponibles(@RequestParam LocalDateTime fecha){
       
        List<Mesa> listaMesas = mesaService.buscarMesasDisponibles(fecha);
        
       return listaMesas.stream()
               .map(mapper::mappearMesaSinReserva)
               .toList();
       
   }
    
    
    
    
    
}
