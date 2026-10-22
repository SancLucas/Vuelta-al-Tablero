package com.borboletta.vueltaaltablero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.HttpSessionEventPublisher;

@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private static final String RUTA_INGRESO = "/usuario/ingreso";
    private static final String RUTA_PERFIL = "/usuario/perfil";
    private static final String RUTA_SALIDA = "/salir";

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    
    @Bean
    public SessionRegistry sessionRegistry(){
        return new SessionRegistryImpl();
    }

    // Avisa al registro cuando una sesión se cierra o vence, para que no se llene de sesiones muertas.
    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher(){
        return new HttpSessionEventPublisher();
    }

    @Bean
    public SecurityFilterChain filtroDeSeguridad(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests(rutas -> rutas
                .requestMatchers("/", "/registro", RUTA_INGRESO,
                                 "/css/**", "/js/**", "/images/**", "/error").permitAll()
                // Catálogo y ejemplares disponibles: públicos, pero SOLO para mirar (GET).
                .requestMatchers(HttpMethod.GET, "/juegos", "/juegos/*",
                                 "/ejemplares", "/ejemplares/*").permitAll()
                // Administración: todo lo que cuelga de /admin/** es solo para administradores.
                .requestMatchers("/usuarios", "/admin/**").hasRole(Roles.ADMINISTRADOR)
                // Lo demás (por ejemplo /mis-ejemplares/**) pide sesión iniciada.
                .anyRequest().authenticated())
            .formLogin(formulario -> formulario
                .loginPage(RUTA_INGRESO)
                .loginProcessingUrl(RUTA_INGRESO)
                .usernameParameter("correo")
                .passwordParameter("contrasenia")
                .defaultSuccessUrl(RUTA_PERFIL, true)
                .failureUrl(RUTA_INGRESO + "?error")
                .permitAll())
            .logout(salida -> salida
                .logoutUrl(RUTA_SALIDA)
                .logoutSuccessUrl("/")
                .permitAll())
            .sessionManagement(sesion -> sesion
                .maximumSessions(-1)                  // -1 = sin límite: solo queremos el registro
                .sessionRegistry(sessionRegistry())
                .expiredUrl(RUTA_INGRESO));           // adonde va quien tenga la sesión vencida
        return http.build();
    }
}
