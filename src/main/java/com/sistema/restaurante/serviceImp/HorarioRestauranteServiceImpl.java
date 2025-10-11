/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.serviceImp;

import com.sistema.restaurante.entities.HorarioRestaurante;
import com.sistema.restaurante.repository.HorarioRestauranteRepository;
import com.sistema.restaurante.services.HorarioRestauranteService;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Anthony
 */
@Service
public class HorarioRestauranteServiceImpl implements HorarioRestauranteService {

    @Autowired
    private HorarioRestauranteRepository horarioRestauranteRepository;

    @Override
    public List<HorarioRestaurante> obtenerHorariosRestaurante() {
        return horarioRestauranteRepository.findAll();
    }

    @Override
    public HorarioRestaurante obtenerHorarioRestaurante(UUID id) {

        HorarioRestaurante horarioRestaurante = horarioRestauranteRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el horario"));

        return horarioRestaurante;

    }

    @Override
    public HorarioRestaurante crearHorarioRestaurante(HorarioRestaurante fecha) {
        return horarioRestauranteRepository.save(fecha);
    }

    @Override
    public List<HorarioRestaurante> editarHorarioRestaurante(List<HorarioRestaurante> listaHorariosRestaurante) {
        
        List<HorarioRestaurante> actualizados = new ArrayList<>();

        for (HorarioRestaurante horarioNuevo : listaHorariosRestaurante) {
            HorarioRestaurante horarioExistente = horarioRestauranteRepository.findById(horarioNuevo.getId())
                    .orElseThrow();
            horarioExistente.setHoraApertura(horarioNuevo.getHoraApertura());
            horarioExistente.setHoraCierre(horarioNuevo.getHoraCierre());
            actualizados.add(horarioExistente);
        }

        return horarioRestauranteRepository.saveAll(actualizados);
    }

    @Override
    public void eliminarHorarioRestaurante(UUID id) {
        HorarioRestaurante horarioRestauranteById = horarioRestauranteRepository.findById(id).orElseThrow(() -> new RuntimeException("No se encontro el horario"));

        horarioRestauranteRepository.delete(horarioRestauranteById);

    }

    @Override
    public HorarioRestaurante obtenerPorDia(String dia
    ) {
        HorarioRestaurante horario = horarioRestauranteRepository.findBydiaSemana(dia);

        return horario;
    }

}
