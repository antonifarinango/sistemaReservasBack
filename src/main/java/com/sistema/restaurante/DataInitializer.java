/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante;

/**
 *
 * @author Anthony
 */
import com.sistema.restaurante.entities.DisponibilidadTurno;
import com.sistema.restaurante.entities.Estado;
import com.sistema.restaurante.entities.EstadoAhora;
import com.sistema.restaurante.entities.HorarioRestaurante;
import com.sistema.restaurante.entities.Mesa;
import com.sistema.restaurante.entities.Restaurante;
import com.sistema.restaurante.entities.Turno;
import com.sistema.restaurante.repository.DisponibilidadTurnoRepository;
import com.sistema.restaurante.repository.HorarioRestauranteRepository;
import com.sistema.restaurante.repository.MesaRepository;
import com.sistema.restaurante.repository.RestauranteRepository;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private MesaRepository mesaRepository;

    @Autowired
    private DisponibilidadTurnoRepository disponibilidadTurnoRepository;
    
    @Autowired
    private HorarioRestauranteRepository horarioRestauranteRepository;
    
    @Autowired
    private RestauranteRepository restauranteRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        // Verifica si ya existen mesas
        if (mesaRepository.count() == 0) {
            for (int i = 1; i <= 16; i++) {
                Mesa mesa = new Mesa();
                mesa.setNumero(i);
                mesa.setCapacidad(4); // o el valor que quieras
                mesa.setEstadoActual(EstadoAhora.Libre);
                mesa.setEstado(Estado.DISPONIBLE); // estado inicial
                mesa.setActiva(true);
                mesaRepository.save(mesa);
            }
            System.out.println("Mesas iniciales creadas automáticamente");
        } else {
            System.out.println("Mesas ya existen, no se crean de nuevo");
        }

        if (disponibilidadTurnoRepository.count() == 0) {

            DisponibilidadTurno maniana = new DisponibilidadTurno();
            maniana.setTurno(Turno.Mañana);
            maniana.setHoraInicio(LocalTime.parse("08:00"));
            maniana.setHoraFin(LocalTime.parse("12:00"));
            maniana.setHabilitado(true);

            DisponibilidadTurno tarde = new DisponibilidadTurno();
            tarde.setTurno(Turno.Tarde);
            tarde.setHoraInicio(LocalTime.parse("12:00"));
            tarde.setHoraFin(LocalTime.parse("18:00"));
            tarde.setHabilitado(true);

            DisponibilidadTurno noche = new DisponibilidadTurno();
            noche.setTurno(Turno.Noche);
            noche.setHoraInicio(LocalTime.parse("18:00"));
            noche.setHoraFin(LocalTime.parse("22:00"));
            noche.setHabilitado(true);

            disponibilidadTurnoRepository.saveAll(Arrays.asList(maniana, tarde, noche));
            
        }
        
         if (horarioRestauranteRepository.count() == 0) {

            List<String> diasSemana = Stream.of(
                    "Lunes", "Martes", "Miércoles", "Jueves", "Viernes", "Sábado", "Domingo"
            ).collect(Collectors.toList());

            List<HorarioRestaurante> horarios = diasSemana.stream().map(dia -> {
                HorarioRestaurante horario = new HorarioRestaurante();
                horario.setDiaSemana(dia);
                horario.setHoraApertura(LocalTime.parse("08:00"));
                horario.setHoraCierre(LocalTime.parse("18:00"));
                return horario;
            }).collect(Collectors.toList());

            horarioRestauranteRepository.saveAll(horarios);

            System.out.println("Horarios creados automáticamente para todos los días de la semana.");
        }
         
         if(restauranteRepository.count() == 0){
             
               Restaurante restaurante = new Restaurante();
               
               restaurante.setDescripcion("Descripcion Restaurante");
               restaurante.setDireccion("Direccion restaurante");
               restaurante.setEmail("Email restaurante");
               restaurante.setNombre("Nombre restaurante");
               restaurante.setTelefono("Teléfono restaurante");
               
               restauranteRepository.save(restaurante);
               
         }
    }
         
}
