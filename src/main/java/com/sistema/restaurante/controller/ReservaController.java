/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.controller;

import com.sistema.restaurante.DTO.MesaActualizacionDTO;
import com.sistema.restaurante.DTO.ReservaConMesaDTO;
import com.sistema.restaurante.DTO.ReservaDTO;
import com.sistema.restaurante.DTO.UsuarioDTO;
import com.sistema.restaurante.entities.DisponibilidadTurno;
import com.sistema.restaurante.entities.Estado;
import com.sistema.restaurante.entities.FechaBloqueada;
import com.sistema.restaurante.entities.HorarioRestaurante;
import com.sistema.restaurante.entities.Mesa;
import com.sistema.restaurante.entities.Reserva;
import com.sistema.restaurante.entities.Usuario;
import com.sistema.restaurante.mappers.SistemaReservaMapper;
import com.sistema.restaurante.services.DisponibilidadTurnoService;
import com.sistema.restaurante.services.FechaBloqueadaService;
import com.sistema.restaurante.services.HorarioRestauranteService;
import com.sistema.restaurante.services.MesaService;
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
import com.sistema.restaurante.services.ReservaService;
import com.sistema.restaurante.services.UsuarioService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.Locale;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;

/**
 *
 * @author Anthony
 */
@RestController
@RequestMapping("/api/v1/reservas")
@CrossOrigin(origins = "http://localhost:5173")
public class ReservaController {

    @Autowired
    private ReservaService reservaService;
    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private MesaService mesaService;
    @Autowired
    private HorarioRestauranteService horarioRestauranteService;
    @Autowired
    private FechaBloqueadaService fechaBloqueadaService;
    @Autowired
    private DisponibilidadTurnoService disponibilidadTurnoService;

    @Autowired
    private SistemaReservaMapper mapper;

    @GetMapping("/todos")
    public List<ReservaDTO> listaReserva() {
        return reservaService.obtenerReservas();
    }

    @GetMapping("/dia")
    public List<ReservaConMesaDTO> listaReservasPorDia() {

        return reservaService.obtenerReservasPorDia();

    }

    @GetMapping("/pendientes")
    public List<ReservaConMesaDTO> listaReservasPendientes() {

        return reservaService.obtenerReservasPendientes();

    }

    @GetMapping("/proximas")
    public List<ReservaConMesaDTO> listaReservasProximas() {

        return reservaService.obtenerReservasProximas();

    }

    @GetMapping("/historial")
    public List<ReservaConMesaDTO> historialReservas(Authentication authentication) {

        String email = authentication.getName();
        Usuario usuario = usuarioService.findByEmail(email);

        return reservaService.obtenerHistorialReservas(usuario.getId());

    }

    @GetMapping("/historial/{id}")
    public List<ReservaConMesaDTO> historialReservas(@PathVariable UUID id) {

        UsuarioDTO usuario = usuarioService.obtenerUsuario(id);

        return reservaService.obtenerHistorialReservas(usuario.getId());

    }

    @GetMapping("/{id}")
    public ResponseEntity<ReservaConMesaDTO> reserva(@PathVariable UUID id) {

        try {

            Reserva reserva = reservaService.obtenerReserva(id);
            ReservaConMesaDTO reservaDTO = mapper.mappearReservaMesa(reserva);

            return ResponseEntity.ok(reservaDTO);

        } catch (RuntimeException e) {

            return ResponseEntity.notFound().build();

        }

    }

   @PostMapping("/crear")
public ResponseEntity<?> guardarReserva(@RequestBody ReservaDTO reservaDTO, Authentication authentication) {

    Reserva reserva = mapper.mappearReservaDTO(reservaDTO);

    // Usuario autenticado
    String email = authentication.getName();
    Usuario usuario = usuarioService.findByEmail(email);
    reserva.setUsuario(usuario);

    // Mesa
    Mesa mesa = mesaService.obtenerMesaPorId(reservaDTO.getMesa());
    MesaActualizacionDTO mesaSinReservas = mapper.mappearMesaSinReserva(mesa);
    mesaSinReservas.setEstado(Estado.RESERVADA);
    mesaService.editarMesa(mesa.getId(), mesaSinReservas);
    reserva.setMesa(mesa);

    // Fecha
    LocalDateTime fecha = reserva.getFecha();
    LocalTime horaReserva = fecha.toLocalTime();
    
    String diaDeLaSemana = fecha.getDayOfWeek()
        .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
    diaDeLaSemana = Character.toUpperCase(diaDeLaSemana.charAt(0)) + diaDeLaSemana.substring(1);

    HorarioRestaurante horario = horarioRestauranteService.obtenerPorDia(diaDeLaSemana);
    
    LocalTime inicio = horario.getHoraApertura();
    LocalTime fin = horario.getHoraCierre();

    boolean dentroDelRango = !horaReserva.isBefore(inicio) && !horaReserva.isAfter(fin);
    
    boolean fechaBloqueada = fechaBloqueadaService.existenciaSegunFecha(fecha.toLocalDate());

    // Turno
    List<DisponibilidadTurno> listaTurnos = disponibilidadTurnoService.obtenerTurnos();
    DisponibilidadTurno turnoEncontrado = listaTurnos.stream()
        .filter(t -> !horaReserva.isBefore(t.getHoraInicio()) && !horaReserva.isAfter(t.getHoraFin()))
        .findFirst()
        .orElse(null);

    if (turnoEncontrado == null) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "La hora seleccionada no pertenece a ningún turno disponible."));
    }
    reserva.setTurno(turnoEncontrado);

    if (fechaBloqueada) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "La fecha " + fecha.toLocalDate() + " no esta disponible para reservas"));
    }
    
    if (!dentroDelRango) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "La reserva esta fuera del horario de este día"));
    }
    
    // Guardar
    Reserva nuevaReserva = reservaService.crearReserva(reserva);
    ReservaDTO respuesta = mapper.mappearReserva(nuevaReserva);

    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(nuevaReserva.getId())
        .toUri();

    return ResponseEntity.created(location).body(respuesta);
}


@PutMapping("/actualizar/{id}")
public ResponseEntity<?> actualizarReserva(@PathVariable UUID id, @RequestBody ReservaDTO reservaDTO, Authentication authentication) {

    Reserva reserva = mapper.mappearReservaDTO(reservaDTO);

    // Usuario autenticado
    String email = authentication.getName();
    Usuario usuario = usuarioService.findByEmail(email);
    reserva.setUsuario(usuario);

    // Mesa
    Mesa mesa = mesaService.obtenerMesaPorId(reservaDTO.getMesa());
    reserva.setMesa(mesa);

    // Fecha
    LocalDateTime fecha = reserva.getFecha();
    LocalTime horaReserva = fecha.toLocalTime();
    String diaDeLaSemana = fecha.getDayOfWeek()
        .getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
    diaDeLaSemana = Character.toUpperCase(diaDeLaSemana.charAt(0)) + diaDeLaSemana.substring(1);

    HorarioRestaurante horario = horarioRestauranteService.obtenerPorDia(diaDeLaSemana);
    if (horario == null) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "No existe horario configurado para el día  " + diaDeLaSemana));
    }

    LocalTime inicio = horario.getHoraApertura();
    LocalTime fin = horario.getHoraCierre();

    boolean dentroDelRango = !horaReserva.isBefore(inicio) && !horaReserva.isAfter(fin);
    boolean fechaBloqueada = fechaBloqueadaService.existenciaSegunFecha(fecha.toLocalDate());

    // Turno
    List<DisponibilidadTurno> listaTurnos = disponibilidadTurnoService.obtenerTurnos();
    DisponibilidadTurno turnoEncontrado = listaTurnos.stream()
        .filter(t -> !horaReserva.isBefore(t.getHoraInicio()) && !horaReserva.isAfter(t.getHoraFin()))
        .findFirst()
        .orElse(null);

    if (turnoEncontrado == null) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "La hora seleccionada no pertenece a ningún turno disponible."));
    }
    reserva.setTurno(turnoEncontrado);

    if (fechaBloqueada) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "La fecha " + fecha.toLocalDate() + " no esta disponible para reservas"));
    }
    
    if (!dentroDelRango) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .body(Map.of("error", "La reserva esta fuera del horario de este día"));
    }

    // Actualizar
    Reserva actualizada = reservaService.editarReserva(id, reserva);
    ReservaConMesaDTO respuesta = mapper.mappearReservaMesa(actualizada);

    return ResponseEntity.ok(respuesta);
}

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarReserva(@PathVariable UUID id) {

        reservaService.eliminarReserva(id);
        Map<String, Boolean> response = new HashMap<>();

        response.put("delete", Boolean.TRUE);
        return ResponseEntity.ok(response);

    }

}
