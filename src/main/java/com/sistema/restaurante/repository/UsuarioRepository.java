/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.sistema.restaurante.repository;

import com.sistema.restaurante.entities.Rol;
import com.sistema.restaurante.entities.Usuario;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Anthony
 */
@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {

    Optional<Usuario> findByEmail(String email);

    @Query("""
    SELECT DISTINCT u
    FROM Usuario u
    JOIN u.reservas r
    WHERE u.rol = :rol
""")
    List<Usuario> findUsuariosConReservasByRol(@Param("rol") Rol rol);

}
