/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.controller;

import com.sistema.restaurante.DTO.ReservaConMesaDTO;
import com.sistema.restaurante.DTO.ReservaDTO;
import com.sistema.restaurante.DTO.UsuarioDTO;
import com.sistema.restaurante.entities.Reserva;
import com.sistema.restaurante.entities.Usuario;
import com.sistema.restaurante.mappers.SistemaReservaMapper;
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
    private NotificacionController notificador;

    @Autowired
    private SistemaReservaMapper mapper;

    @GetMapping("/todos")
    public List<ReservaConMesaDTO> listaReserva() {
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
        try {
            String email = (authentication != null) ? authentication.getName() : null;
            Reserva nuevaReserva = reservaService.crearReserva(reservaDTO, email);
            ReservaConMesaDTO respuesta = mapper.mappearReservaMesa(nuevaReserva);
            respuesta.setTipoNotificacion("CREACION");

            notificador.enviarNotificacionReserva(respuesta);

            URI location = ServletUriComponentsBuilder.fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(nuevaReserva.getId())
                    .toUri();

            return ResponseEntity
                    .created(location)
                    .body(Map.of(
                            "mensaje", "Reserva creada correctamente",
                            "reserva", respuesta));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error al procesar la reserva: " + e.getMessage()));
        }
    }

    @PutMapping("/actualizar/{id}")
    public ResponseEntity<?> actualizarReserva(@PathVariable UUID id, @RequestBody ReservaDTO reservaDTO, Authentication authentication) {
        try {
            String email = (authentication != null) ? authentication.getName() : null;
            Reserva actualizada = reservaService.editarReserva(id, reservaDTO, email);
            ReservaConMesaDTO respuesta = mapper.mappearReservaMesa(actualizada);
            
            boolean isCliente = authentication != null && authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_Cliente"));

            if (actualizada.getEstadoReserva() == com.sistema.restaurante.entities.EstadoReserva.Confirmada) {
                respuesta.setTipoNotificacion("CONFIRMACION");
            } else if (isCliente) {
                respuesta.setTipoNotificacion("MODIFICACION");
            } else {
                respuesta.setTipoNotificacion("MODIFICACION_ADMIN");
            }

            notificador.enviarNotificacionReserva(respuesta);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(Map.of(
                            "mensaje", "Reserva actualizada correctamente",
                            "reserva", respuesta
                    ));
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Ocurrió un error al actualizar la reserva: " + e.getMessage()));
        }
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<Map<String, Boolean>> eliminarReserva(@PathVariable UUID id) {

        try {
            Reserva reserva = reservaService.obtenerReserva(id);
            if (reserva != null) {
                ReservaConMesaDTO dto = mapper.mappearReservaMesa(reserva);
                dto.setTipoNotificacion("ELIMINACION");
                notificador.enviarNotificacionReserva(dto);
            }
        } catch (Exception e) {
            // Loguear o ignorar si no se encuentra
        }

        reservaService.eliminarReserva(id);
        Map<String, Boolean> response = new HashMap<>();

        response.put("delete", Boolean.TRUE);
        return ResponseEntity.ok(response);

    }

}
