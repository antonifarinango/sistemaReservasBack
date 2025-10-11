/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.serviceImp;

import com.sistema.restaurante.DTO.UsuarioDTO;
import com.sistema.restaurante.entities.Rol;
import com.sistema.restaurante.entities.Usuario;
import com.sistema.restaurante.mappers.SistemaReservaMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sistema.restaurante.services.UsuarioService;
import com.sistema.restaurante.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Anthony
 */
@Service
public class UsuarioServiceImp implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private SistemaReservaMapper mapper;

    @Override
    public List<UsuarioDTO> obtenerUsuarios() {

        List<Usuario> listaUsuarios = usuarioRepository.findAll();

        return listaUsuarios.stream()
                .map(mapper::mappearUsuario) // devuelve UsuarioDTO
                .toList();

    }

    @Override
    public UsuarioDTO obtenerUsuario(UUID idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        return mapper.mappearUsuario(usuario);

    }

    @Override
    public Usuario editarUsuario(UUID idUsuario, Usuario usuarioNuevo) {

        return usuarioRepository.findById(idUsuario).map(usuario -> {
            usuario.setNombre(usuarioNuevo.getNombre());
            usuario.setEmail(usuarioNuevo.getEmail());
            usuario.setTelefono(usuarioNuevo.getTelefono());

            if (usuarioNuevo.getPassword() != null && !usuarioNuevo.getPassword().isBlank()) {
                usuario.setPassword(passwordEncoder.encode(usuarioNuevo.getPassword()));
            }

            usuario.setRol(usuarioNuevo.getRol());
            return usuarioRepository.save(usuario);
        }).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    }

    @Override
    public void eliminarUsuario(UUID idUsuario) {

        Usuario usuario = usuarioRepository.findById(idUsuario).orElse(null);

        if (usuario != null) {
            usuarioRepository.delete(usuario);
        }

    }

    @Override
    public Usuario findByEmail(String email) {

        return usuarioRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

    }

    @Override
    public Usuario registrarUsuario(Usuario usuario) {

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); //ENCRIPTAR LA CONTRASEÑA
        usuario.setRol(Rol.CLIENTE);

        return usuarioRepository.save(usuario);

    }

    @Override
    public Usuario crearUsuario(Usuario usuario) {

        usuario.setPassword(passwordEncoder.encode(usuario.getPassword())); //ENCRIPTAR LA CONTRASEÑA

        if (usuario.getRol() == null) {
            usuario.setRol(Rol.ADMIN);
        } else {
            usuario.setRol(usuario.getRol());
        }

        return usuarioRepository.save(usuario);

    }

    @Override
    public List<UsuarioDTO> obtenerUsuarioRol() {
        
        List<Usuario> listaUsuarios = usuarioRepository.findUsuariosConReservasByRol(Rol.CLIENTE);
        
        return listaUsuarios.stream()
                .map(mapper::mappearUsuario)
                .toList();
        
    }

}
