/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.services;

import com.sistema.restaurante.DTO.UsuarioDTO;
import com.sistema.restaurante.DTO.UsuarioRolDTO;
import com.sistema.restaurante.entities.Usuario;
import java.util.List;
import java.util.UUID;

/**
 *
 * @author Anthony
 */
public interface UsuarioService{
    
    //OBTENER LISTA DE USUARIOS
    List<UsuarioDTO> obtenerUsuarios();
    
    //OBTENER LISTA DE USUARIOS POR ROL
    List<UsuarioDTO> obtenerUsuariosRol();
    
    //OBTENER LISTA DE USUARIOS POR ROL
    List<UsuarioRolDTO> obtenerUsuariosExcluyendoRol();
    
    //OBTENER USUARIO POR ID
    UsuarioDTO obtenerUsuario(UUID idUsuario);
    
    //CREAR USUARIO
    Usuario crearUsuario(Usuario usuario);
    
    //EDITAR USUARIO
    Usuario editarUsuario(UUID idUsuario, Usuario usuarioNuevo);
    
    //ELIMINAR USUARIO
    void eliminarUsuario(UUID idUsuario);
    
    //BUSCAR USUARIO POR NOMBRE
    Usuario findByEmail(String userEmail);
    
   //REGISTRAR NUEVO USUARIO
    Usuario registrarUsuario(Usuario usuario);
}
