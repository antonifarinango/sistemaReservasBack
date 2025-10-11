/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.serviceImp;

import com.sistema.restaurante.entities.FechaBloqueada;
import com.sistema.restaurante.repository.FechaBloqueadaRepository;
import com.sistema.restaurante.services.FechaBloqueadaService;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Anthony
 */
@Service
public class FechaBloqueadaServiceImpl implements FechaBloqueadaService {

    @Autowired
    private FechaBloqueadaRepository fechaBloqueadaRepository;

    @Override
    public List<FechaBloqueada> obtenerFechasBloqueadas() {
        return fechaBloqueadaRepository.findAll();
    }

    @Override
    public FechaBloqueada obtenerFechaBloqueada(UUID id) {

        FechaBloqueada fechaById = fechaBloqueadaRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro la fecha bloqueada"));
        return fechaById;

    }

    @Override
    public FechaBloqueada crearFechaBloqueada(FechaBloqueada fecha) {
        return fechaBloqueadaRepository.save(fecha);
    }

    @Override
    public FechaBloqueada editarFechaBloqueada(UUID id, FechaBloqueada fecha) {

        FechaBloqueada fechaById = fechaBloqueadaRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro la fecha bloqueada"));

        fechaById.setFecha(fecha.getFecha());
        fechaById.setMotivo(fecha.getMotivo());

        return fechaBloqueadaRepository.save(fechaById);
    }

    @Override
    public void eliminarFechaBloqueada(UUID id) {

        FechaBloqueada fechaById = fechaBloqueadaRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro la fecha bloqueada"));

        fechaBloqueadaRepository.delete(fechaById);

    }

    @Override
    public boolean existenciaSegunFecha(LocalDate fecha) {
        
        return fechaBloqueadaRepository.existsByFecha(fecha);
        
    }

}
