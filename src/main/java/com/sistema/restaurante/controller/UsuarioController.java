/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.controller;

import com.sistema.restaurante.DTO.UsuarioDTO;
import com.sistema.restaurante.entities.Usuario;
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
import com.sistema.restaurante.services.UsuarioService;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author Anthony
 */
@RestController
@RequestMapping("/api/v1/usuarios/administracion")
@CrossOrigin(origins = "http://localhost:5173") 
public class UsuarioController {

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("/todos")
    public List<UsuarioDTO> listaUsuarios() {
        return usuarioService.obtenerUsuarios();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UsuarioDTO> usuario(@PathVariable UUID id) {

        try {
            UsuarioDTO usuarioDTO = usuarioService.obtenerUsuario(id); // devuelve Usuario directamente
            return ResponseEntity.ok(usuarioDTO);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }

    }
    
    @GetMapping("/clientes")
    public List<UsuarioDTO> listaUsuariosRol (){
        
        return usuarioService.obtenerUsuarioRol();
        
    }
    

    @PutMapping("/actualizar/{id}")
    public Usuario actualizarUsuario(@PathVariable UUID id, @RequestBody Usuario usuario) {

        return usuarioService.editarUsuario(id, usuario);

    }

    @PostMapping("/crearUsuario")
    public ResponseEntity<Usuario> crearUsuario(@RequestBody Usuario usuario) {

        Usuario nuevoUsuario = usuarioService.crearUsuario(usuario);

        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(nuevoUsuario.getId())
                .toUri();

        return ResponseEntity.created(location).body(nuevoUsuario);

    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarUsuario(@PathVariable UUID id) {

        usuarioService.eliminarUsuario(id);

        Map<String, Boolean> response = new HashMap<>();

        response.put("delete", Boolean.TRUE);

        return ResponseEntity.ok(response);

    }

}
