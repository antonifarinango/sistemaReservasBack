/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.serviceImp;

import com.sistema.restaurante.DTO.MesaActualizacionDTO;
import com.sistema.restaurante.DTO.ReservaConMesaDTO;
import com.sistema.restaurante.DTO.ReservaDTO;
import com.sistema.restaurante.DTO.UsuarioDTO;
import com.sistema.restaurante.entities.*;
import com.sistema.restaurante.mappers.SistemaReservaMapper;
import com.sistema.restaurante.repository.ReservaRepository;
import com.sistema.restaurante.services.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.TextStyle;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.ErrorResponseException;

/**
 *
 * @author Anthony
 */
@Service
public class ReservaServiceImp implements ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    private SistemaReservaMapper mapper;

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

    @Override
    public List<ReservaConMesaDTO> obtenerReservas() {

        List<Reserva> listaReservas = reservaRepository.findAll();

        return listaReservas.stream()
                .map(mapper::mappearReservaMesa)
                .toList();

    }

    @Override
    public Reserva obtenerReserva(UUID idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva).orElseThrow(() -> new RuntimeException("No se encontro la reserva"));
        return reserva;

    }

    @Override
    @Transactional
    public Reserva crearReserva(ReservaDTO reservaDTO, String emailAutenticado) {
        Reserva reserva = mapper.mappearReservaDTO(reservaDTO);

        // Usuario
        Usuario usuario;
        if (reservaDTO.getUsuario() == null) {
            usuario = usuarioService.findByEmail(emailAutenticado);
        } else {
            UsuarioDTO usuarioDTO = usuarioService.obtenerUsuario(reservaDTO.getUsuario());
            usuario = mapper.mappearUsuarioDTO(usuarioDTO);
        }
        reserva.setUsuario(usuario);

        // Mesa
        Mesa mesa = mesaService.obtenerMesaPorId(reservaDTO.getMesa());
        MesaActualizacionDTO mesaSinReservas = mapper.mappearMesaSinReserva(mesa);
        mesaSinReservas.setEstado(Estado.RESERVADA);
        mesaService.editarMesa(mesa.getId(), mesaSinReservas);
        reserva.setMesa(mesa);

        // Validaciones de fecha y turno
        validarReserva(reserva);

        // Defaults
        reserva.setEstadoReserva(EstadoReserva.Pendiente);
        reserva.setServicio(Servicio.SinServicio);

        return reservaRepository.save(reserva);
    }

    @Override
    @Transactional
    public Reserva editarReserva(UUID idReserva, ReservaDTO reservaDTO, String emailAutenticado) {
        Reserva reservaExistente = reservaRepository.findById(idReserva)
                .orElseThrow(() -> new RuntimeException("Reserva no encontrada"));

        Reserva reservaNuevosDatos = mapper.mappearReservaDTO(reservaDTO);

        // Usuario
        Usuario usuario;
        if (reservaDTO.getUsuario() == null) {
            usuario = usuarioService.findByEmail(emailAutenticado);
        } else {
            UsuarioDTO usuarioDTO = usuarioService.obtenerUsuario(reservaDTO.getUsuario());
            usuario = mapper.mappearUsuarioDTO(usuarioDTO);
        }
        reservaExistente.setUsuario(usuario);

        // Mesa
        Mesa mesa = mesaService.obtenerMesaPorId(reservaDTO.getMesa());
        reservaExistente.setMesa(mesa);

        // Otros datos
        reservaExistente.setCantidadPersonas(reservaNuevosDatos.getCantidadPersonas());
        reservaExistente.setFecha(reservaNuevosDatos.getFecha());
        reservaExistente.setEstadoReserva(reservaNuevosDatos.getEstadoReserva());
        reservaExistente.setServicio(reservaNuevosDatos.getServicio());

        // Validaciones
        validarReserva(reservaExistente);

        return reservaRepository.save(reservaExistente);
    }

    private void validarReserva(Reserva reserva) {
        LocalDateTime fecha = reserva.getFecha();
        LocalTime horaReserva = fecha.toLocalTime();

        // Validar Horario del Restaurante
        String diaDeLaSemana = fecha.getDayOfWeek().getDisplayName(TextStyle.FULL, new Locale("es", "ES"));
        diaDeLaSemana = Character.toUpperCase(diaDeLaSemana.charAt(0)) + diaDeLaSemana.substring(1);

        HorarioRestaurante horario = horarioRestauranteService.obtenerPorDia(diaDeLaSemana);
        if (horario == null) {
            throw new IllegalArgumentException("No existe horario configurado para el día " + diaDeLaSemana);
        }

        LocalTime inicio = horario.getHoraApertura();
        LocalTime fin = horario.getHoraCierre();

        if (horaReserva.isBefore(inicio) || horaReserva.isAfter(fin)) {
            throw new IllegalArgumentException("La reserva esta fuera del horario de este día");
        }

        // Validar Fecha Bloqueada
        if (fechaBloqueadaService.existenciaSegunFecha(fecha.toLocalDate())) {
            throw new IllegalArgumentException("La fecha " + fecha.toLocalDate() + " no esta disponible para reservas");
        }

        // Validar y Asignar Turno
        List<DisponibilidadTurno> listaTurnos = disponibilidadTurnoService.obtenerTurnos();
        DisponibilidadTurno turnoEncontrado = listaTurnos.stream()
                .filter(t -> !horaReserva.isBefore(t.getHoraInicio()) && !horaReserva.isAfter(t.getHoraFin()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("La hora seleccionada no pertenece a ningún turno disponible."));

        reserva.setTurno(turnoEncontrado);
    }

    @Override
    public void eliminarReserva(UUID idReserva) {

        Reserva reserva = reservaRepository.findById(idReserva).orElse(null);

        if (reserva != null) {
            reservaRepository.delete(reserva);
        }

    }

    @Override
    public List<ReservaConMesaDTO> obtenerReservasPorDia() {

        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.atStartOfDay();
        LocalDateTime fin = hoy.plusDays(1).atStartOfDay().minusSeconds(1);

        List<Reserva> listaReservasHoy = reservaRepository.findReservasHoy(EstadoReserva.Confirmada, inicio, fin);

        return listaReservasHoy.stream().map(mapper::mappearReservaMesa).toList();

    }

    @Override
    public List<ReservaConMesaDTO> obtenerReservasPendientes() {

        List<Reserva> listaReservasPendientes = reservaRepository.findByEstado(EstadoReserva.Pendiente);

        return listaReservasPendientes.stream()
                .map(mapper::mappearReservaMesa)
                .toList();

    }

    @Override
    public List<ReservaConMesaDTO> obtenerReservasProximas() {
        LocalDate hoy = LocalDate.now();
        LocalDateTime inicio = hoy.plusDays(1).atStartOfDay(); // desde mañana
        LocalDateTime fin = hoy.plusWeeks(1).plusDays(1).atStartOfDay().minusSeconds(1); // hasta dentro de 7 días

        List<Reserva> reservas = reservaRepository.findProximasReservas(EstadoReserva.Confirmada, inicio, fin);

        return reservas.stream()
                .map(mapper::mappearReservaMesa)
                .toList();
    }

    @Override
    public List<ReservaConMesaDTO> obtenerHistorialReservas(UUID idCliente) {
        List<Reserva> listaHistorialReserva = reservaRepository.findHistorialByCliente(idCliente);

        return listaHistorialReserva.stream()
                .map(mapper::mappearReservaMesa)
                .toList();
    }

}
