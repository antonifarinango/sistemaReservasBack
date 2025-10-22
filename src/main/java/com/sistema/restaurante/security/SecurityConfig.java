/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.sistema.restaurante.security;

import com.sistema.restaurante.entities.Rol;
import com.sistema.restaurante.entities.Usuario;
import com.sistema.restaurante.repository.UsuarioRepository;
import java.util.List;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

/**
 *
 * @author Anthony
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) throws Exception {
        http
                .cors(cors -> cors.configurationSource(request -> {
            CorsConfiguration config = new CorsConfiguration();
            config.setAllowedOrigins(List.of("http://localhost:5173")); // FRONTEND
            config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            config.setAllowedHeaders(List.of("*"));
            config.setAllowCredentials(true);
            return config;
        }))
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                .requestMatchers("/api/auth/login", "/api/auth/register").permitAll()
                        
                .requestMatchers("/api/v1/usuarios/administracion/**").hasAnyRole("Superadmin", "Admin", "Mesero")
                        
                .requestMatchers("/api/v1/mesas/administracion/**").hasAnyRole("Superadmin", "Admin", "Mesero")
                .requestMatchers("/api/v1/mesas/todos").permitAll()
                .requestMatchers("/api/v1/mesas/disponibles").permitAll()
                        
                .requestMatchers("/api/v1/reservas/**").permitAll()
                .requestMatchers("/api/v1/disponibilidad/**").permitAll()
                        
                        
                .requestMatchers("/api/v1/restaurantes/**").permitAll()
                        
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/app/**").permitAll()
                .requestMatchers("/topic/**").permitAll()
                        
                .anyRequest().authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    CommandLineRunner init(UsuarioRepository usuarioRepository, PasswordEncoder encoder) {
        return args -> {
            if (usuarioRepository.count() == 0) {
                Usuario superAdmin = Usuario.builder()
                        .nombre("superadmin")
                        .email("superadmin@system")
                        .password(encoder.encode("superadmin@system"))
                        .rol(Rol.Superadmin)
                        .build();

                Usuario admin = Usuario.builder()
                        .nombre("admin")
                        .email("admin@system")
                        .password(encoder.encode("admin@system"))
                        .rol(Rol.Admin)
                        .build();

                Usuario mesero = Usuario.builder()
                        .nombre("mesero")
                        .email("mesero@system")
                        .password(encoder.encode("mesero@system"))
                        .rol(Rol.Mesero)
                        .build();

                usuarioRepository.save(superAdmin);
                usuarioRepository.save(admin);
                usuarioRepository.save(mesero);
            }
        };
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(UserDetailsService userDetailsService, JwtUtil jwtUtil) {
        return new JwtAuthenticationFilter(userDetailsService, jwtUtil);
    }
}
