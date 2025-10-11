/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.serviceImp;

import com.sistema.restaurante.DTO.MesaDTO;
import com.sistema.restaurante.DTO.MesaActualizacionDTO;
import com.sistema.restaurante.DTO.ReservaDTO;
import com.sistema.restaurante.entities.Estado;
import com.sistema.restaurante.entities.EstadoAhora;
import com.sistema.restaurante.entities.Mesa;
import com.sistema.restaurante.mappers.SistemaReservaMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sistema.restaurante.services.MesaService;
import com.sistema.restaurante.repository.MesaRepository;

/**
 *
 * @author Anthony
 */
@Service
public class MesaServiceImp implements MesaService {

    @Autowired
    private MesaRepository mesaRepository;
    
    @Autowired
    private SistemaReservaMapper mapper;

    @Override
    public List<MesaDTO> obtenerMesas() {
        
        List<Mesa> listaMesa = mesaRepository.findAll();
        
        return listaMesa.stream()
                .map(mapper :: mappearMesa)
                .toList();
         
    }

    @Override
    public Mesa obtenerMesaPorId(UUID idMesa) {

        Mesa mesa = mesaRepository.findById(idMesa).orElseThrow(()-> new RuntimeException("Mesa no encontrada"));
        
        if(mesa.getReservas().isEmpty()){
            mesa.setEstado(Estado.DISPONIBLE);
        }
        
        mesaRepository.save(mesa);
        
        return mesa;

    }

    @Override
    public Mesa crearMesa(Mesa mesa) {
        
        Mesa nuevaMesa = Mesa.builder()
                .numero(mesa.getNumero())
                .capacidad(mesa.getCapacidad())
               . estadoActual(EstadoAhora.Libre)
                .estado(Estado.DISPONIBLE)
                .build();
        
        return mesaRepository.save(nuevaMesa);
        
    }

    @Override
    public MesaDTO editarMesa(UUID idMesa, MesaActualizacionDTO mesaActualizacionDTO ) {

         Mesa mesa = mesaRepository.findById(idMesa)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));

        // actualizar solo atributos simples
        mesa.setNumero(mesaActualizacionDTO.getNumero());
        mesa.setCapacidad(mesaActualizacionDTO.getCapacidad());
        mesa.setEstadoActual(mesaActualizacionDTO.getEstadoActual());
        mesa.setEstado(mesaActualizacionDTO.getEstado());

        Mesa updated = mesaRepository.save(mesa);

        // mapear a DTO de respuesta (puedes usar MapStruct o manual)
        MesaDTO mesaDTO = mapper.mappearMesa(updated);

        // mapear reservas ya existentes
        
            List<ReservaDTO> reservas= updated.getReservas().stream()
                .map(mapper::mappearReserva)
                .toList();
            

       mesaDTO.setReservas(reservas);

        return mesaDTO;

    }

    @Override
    public void eliminarMesa(UUID idMesa) {

        Mesa mesa = mesaRepository.findById(idMesa).orElse(null);

        if (mesa != null) {
            mesaRepository.delete(mesa);
        }

    }

    @Override
    public boolean estaDisponible(UUID mesaId) {
        
        Mesa mesa = mesaRepository.findById(mesaId).orElse(null);
        
        return mesa.getEstado() != Estado.RESERVADA;
        
    }

}
