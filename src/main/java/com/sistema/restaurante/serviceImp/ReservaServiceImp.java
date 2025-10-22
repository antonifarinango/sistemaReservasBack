/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.serviceImp;

import com.sistema.restaurante.DTO.ReservaConMesaDTO;
import com.sistema.restaurante.entities.EstadoReserva;
import com.sistema.restaurante.entities.Reserva;
import com.sistema.restaurante.entities.Servicio;
import com.sistema.restaurante.mappers.SistemaReservaMapper;
import java.util.List;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.sistema.restaurante.services.ReservaService;
import com.sistema.restaurante.repository.ReservaRepository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;

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
    public Reserva crearReserva(Reserva reserva) {
        reserva.setEstadoReserva(EstadoReserva.Pendiente);
        reserva.setServicio(Servicio.SinServicio);
        return reservaRepository.save(reserva);
    }

    @Override
    public Reserva editarReserva(UUID idReserva, Reserva nuevaReserva) {

        Reserva reserva = reservaRepository.findById(idReserva).orElse(null);

        reserva.setCantidadPersonas(nuevaReserva.getCantidadPersonas());
        reserva.setFecha(nuevaReserva.getFecha());
        reserva.setMesa(nuevaReserva.getMesa());
        reserva.setTurno(nuevaReserva.getTurno());
        reserva.setEstadoReserva(nuevaReserva.getEstadoReserva());
        reserva.setServicio(nuevaReserva.getServicio());

        return reservaRepository.save(reserva);

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
