/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.controller;

/**
 *
 * @author Anthony
 */
import com.sistema.restaurante.DTO.ReservaConMesaDTO;
import com.sistema.restaurante.DTO.ReservaDTO;
import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component
public class NotificacionController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void enviarNotificacionReserva(ReservaConMesaDTO reserva) {

        messagingTemplate.convertAndSend("/topic/reservas", reserva);
        
    }
}
