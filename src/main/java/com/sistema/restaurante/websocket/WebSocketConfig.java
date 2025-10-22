/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.websocket;

/**
 *
 * @author Anthony
 */

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint que usarán los clientes para conectarse al WebSocket
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*") // Permitir acceso desde tu frontend
                .withSockJS(); // Para compatibilidad
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Prefijo para los mensajes que envía el servidor
        registry.enableSimpleBroker("/topic");
        // Prefijo para los mensajes que envía el cliente
        registry.setApplicationDestinationPrefixes("/app");
    }
}
