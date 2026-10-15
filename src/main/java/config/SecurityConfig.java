package com.borboletta.vueltaaltablero.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

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
    public SecurityFilterChain filtroDeSeguridad(HttpSecurity http) throws Exception{
        http
            .authorizeHttpRequests(rutas -> rutas
                .requestMatchers("/", "/registro", RUTA_INGRESO, "/juegos", "/ejemplares",
                                 "/css/**", "/images/**", "/error").permitAll()
                .requestMatchers("/usuarios").hasRole(Roles.ADMINISTRADOR)
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
                .permitAll());
        return http.build();
    }
}
