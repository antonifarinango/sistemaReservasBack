/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.mappers;

import com.sistema.restaurante.DTO.MesaDTO;
import com.sistema.restaurante.DTO.MesaActualizacionDTO;
import com.sistema.restaurante.DTO.ReservaConMesaDTO;
import com.sistema.restaurante.DTO.ReservaDTO;
import com.sistema.restaurante.DTO.UsuarioActualizacionDTO;
import com.sistema.restaurante.DTO.UsuarioDTO;
import com.sistema.restaurante.entities.Mesa;
import com.sistema.restaurante.entities.Reserva;
import com.sistema.restaurante.entities.Usuario;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 *
 * @author Anthony
 */
@Service
public class SistemaReservaMapper {
    
    public UsuarioDTO mappearUsuario(Usuario usuario) {

        UsuarioDTO usuarioDTO = new UsuarioDTO();
        BeanUtils.copyProperties(usuario, usuarioDTO, "reserva");
        
      List<Map<String, String>> reservasList = new ArrayList<>();
      int index = 0;
      for(Reserva reserva : usuario.getReservas()){
          Map<String, String> map = new HashMap<>();
          map.put(String.valueOf(index), reserva.getId().toString());
            reservasList.add(map);
            index++;
      }
      
        usuarioDTO.setReserva(reservasList);

        return usuarioDTO;

    }

    public Usuario mappearUsuarioDTO(UsuarioDTO usuarioDTO) {

        Usuario usuario = new Usuario();
        BeanUtils.copyProperties(usuarioDTO, usuario, "mesa", "usuario");
        
        return usuario;

    }

    public ReservaDTO mappearReserva(Reserva reserva) {
        ReservaDTO reservaDTO = new ReservaDTO();
        BeanUtils.copyProperties(reserva, reservaDTO, "usuario", "mesa","turno");
        
        reservaDTO.setMesa(reserva.getMesa().getId());
        reservaDTO.setUsuario(reserva.getUsuario().getId());
        reservaDTO.setTurno(reserva.getTurno().getTurno());

        return reservaDTO;
    }
    
    public ReservaConMesaDTO mappearReservaMesa (Reserva reserva) {
        ReservaConMesaDTO reservaConMesaDTO = new ReservaConMesaDTO();
        BeanUtils.copyProperties(reserva, reservaConMesaDTO, "usuario", "mesa");
        
        MesaActualizacionDTO mesaSinReserva = this.mappearMesaSinReserva(reserva.getMesa());
        UsuarioActualizacionDTO usuarioSinReserva = this.mappearUsuarioSinReserva(reserva.getUsuario());
        
        reservaConMesaDTO.setUsuario(usuarioSinReserva);
        reservaConMesaDTO.setMesa(mesaSinReserva);

        return reservaConMesaDTO;
    }
    
    public ReservaDTO mappearReservaConMesaDTO (ReservaConMesaDTO reserva) {
        ReservaDTO reservaDTO = new ReservaDTO();
        BeanUtils.copyProperties(reserva, reservaDTO, "usuario", "mesa");
        
        
        reservaDTO.setUsuario(reserva.getUsuario().getId());
        reservaDTO.setMesa(reserva.getMesa().getId());

        return reservaDTO;
    }
    

    public Reserva mappearReservaDTO(ReservaDTO reservaDTO) {

        Reserva reserva = new Reserva();
        BeanUtils.copyProperties(reservaDTO, reserva, "usuario", "mesa");
        
        return reserva;
    }

    public MesaDTO mappearMesa(Mesa mesa) {
        MesaDTO mesaDTO = new MesaDTO();
        BeanUtils.copyProperties(mesa, mesaDTO, "reservas");
        
        List<ReservaDTO> listaReservaDTO = mesa.getReservas().stream()
                .map(this :: mappearReserva)
                .toList();
        
        mesaDTO.setReservas(listaReservaDTO);
        
        return mesaDTO;
    }
    
     public MesaActualizacionDTO mappearMesaSinReserva(Mesa mesa) {
        MesaActualizacionDTO mesaSinReservasDTO = new MesaActualizacionDTO();
        BeanUtils.copyProperties(mesa, mesaSinReservasDTO );
        return mesaSinReservasDTO ;
    }
     
     public UsuarioActualizacionDTO mappearUsuarioSinReserva(Usuario mesa) {
        UsuarioActualizacionDTO usuarioSinReservasDTO = new UsuarioActualizacionDTO();
        BeanUtils.copyProperties(mesa, usuarioSinReservasDTO);
        return usuarioSinReservasDTO ;
    }
    
     public Mesa mappearMesaSinReservaDTO (MesaActualizacionDTO mesaSinReservaDTO) {
         
         Mesa mesa = new Mesa();
         
        BeanUtils.copyProperties( mesaSinReservaDTO, mesa);
        
        return mesa ;
    }
     
    

}
































