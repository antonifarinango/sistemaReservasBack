/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.serviceImp;

import com.sistema.restaurante.entities.DisponibilidadTurno;
import com.sistema.restaurante.repository.DisponibilidadTurnoRepository;
import com.sistema.restaurante.services.DisponibilidadTurnoService;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Anthony
 */
@Service
public class DisponibilidadTurnoServiceImpl implements DisponibilidadTurnoService {

    @Autowired
    private DisponibilidadTurnoRepository disponibilidadTurnoRepository;
    @Override
    public List<DisponibilidadTurno> obtenerTurnos() {
        
        return disponibilidadTurnoRepository.findAll();
    }

    @Override
    public DisponibilidadTurno obtenerTurno(UUID id) {
        
        DisponibilidadTurno turnoById = disponibilidadTurnoRepository.findById(id).orElseThrow(()-> new RuntimeException("No se encontro el turno"));
        return turnoById;
    }

    @Override
    public DisponibilidadTurno crearTurno(DisponibilidadTurno turno) {
        
        return disponibilidadTurnoRepository.save(turno);
        
    }

    @Override
    public List<DisponibilidadTurno> editarTurno(List<DisponibilidadTurno> listaTurnos) {
        
        List<DisponibilidadTurno> listaActual = disponibilidadTurnoRepository.findAll();
        
        for(DisponibilidadTurno turnoNuevo : listaTurnos ){
            
            DisponibilidadTurno turnoById = disponibilidadTurnoRepository.findById(turnoNuevo.getId()).orElseThrow(()-> new RuntimeException("No se encontro el turno"));
            
            turnoById.setHabilitado(turnoNuevo.isHabilitado());
            turnoById.setHoraFin(turnoNuevo.getHoraFin());
            turnoById.setHoraInicio(turnoNuevo.getHoraInicio());
            turnoById.setTurno(turnoNuevo.getTurno());
            
        }
        
        return disponibilidadTurnoRepository.saveAll(listaActual);
    }

    @Override
    public void eliminarTurno(UUID id) {
        
        DisponibilidadTurno turnoById = disponibilidadTurnoRepository.findById(id).orElseThrow(()-> new RuntimeException("No se encontro el turno"));
        disponibilidadTurnoRepository.delete(turnoById);
        
    }

}
