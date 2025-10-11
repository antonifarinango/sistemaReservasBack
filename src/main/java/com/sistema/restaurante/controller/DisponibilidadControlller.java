/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.controller;

import com.sistema.restaurante.entities.DisponibilidadTurno;
import com.sistema.restaurante.entities.FechaBloqueada;
import com.sistema.restaurante.entities.HorarioRestaurante;
import com.sistema.restaurante.services.DisponibilidadTurnoService;
import com.sistema.restaurante.services.FechaBloqueadaService;
import com.sistema.restaurante.services.HorarioRestauranteService;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Anthony
 */
@RestController
@RequestMapping("/api/v1/disponibilidad")
@CrossOrigin(origins = "http://localhost:5173")

public class DisponibilidadControlller {

    @Autowired
    private DisponibilidadTurnoService disponibilidadTurnoService;

    @Autowired
    private HorarioRestauranteService horarioRestauranteService;

    @Autowired
    private FechaBloqueadaService fechaBloqueadaService;

    @GetMapping("/turnos")
    public List<DisponibilidadTurno> listaTurnos() {
        return disponibilidadTurnoService.obtenerTurnos();
    }

    @GetMapping("/horarios")
    public List<HorarioRestaurante> listaHorarios() {
        return horarioRestauranteService.obtenerHorariosRestaurante();
    }

    @GetMapping("/fechasBloqueadas")
    public List<FechaBloqueada> listaFechasBloqueadas() {
        return fechaBloqueadaService.obtenerFechasBloqueadas();
    }

    @PutMapping("/turno/actualizar-{id}")
    public ResponseEntity<List<DisponibilidadTurno>> actualizarTurno(@RequestBody List<DisponibilidadTurno> listaTurnos) {

        List<DisponibilidadTurno> turnosActualizados = disponibilidadTurnoService.editarTurno(listaTurnos);

        return ResponseEntity.ok(turnosActualizados);
    }

    @PutMapping("/horario/actualizar")
    public ResponseEntity<List<HorarioRestaurante>> actualizarHorarios(@RequestBody List<HorarioRestaurante> listaHorarios) {

        List<HorarioRestaurante> horariosActualizados = horarioRestauranteService.editarHorarioRestaurante(listaHorarios);

        return ResponseEntity.ok(horariosActualizados);
    }

    @PutMapping("/fechaBloqueada/actualizar-{id}")
    public ResponseEntity<FechaBloqueada> actualizarTurno(@PathVariable UUID id, @RequestBody FechaBloqueada fecha) {

        FechaBloqueada fechaBloqueadaActualizada = fechaBloqueadaService.editarFechaBloqueada(id, fecha);

        return ResponseEntity.ok(fechaBloqueadaActualizada);
    }

    @PostMapping("/fechaBloqueada/crear")
    public ResponseEntity<FechaBloqueada> crearFechaBloqueda(@RequestBody FechaBloqueada fecha) {

        FechaBloqueada fechaBloqueada = fechaBloqueadaService.crearFechaBloqueada(fecha);

        return ResponseEntity.ok(fechaBloqueada);

    }

    @DeleteMapping("/fechaBloqueada/eliminar-{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarFechaBloqueada(@PathVariable UUID id) {

        fechaBloqueadaService.eliminarFechaBloqueada(id);

        Map<String, Boolean> response = new HashMap<>();

        response.put("delete", Boolean.TRUE);
        return ResponseEntity.ok(response);

    }

}
