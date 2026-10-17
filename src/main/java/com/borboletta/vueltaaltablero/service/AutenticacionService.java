package com.borboletta.vueltaaltablero.service;

import com.borboletta.vueltaaltablero.config.Roles;
import com.borboletta.vueltaaltablero.entity.Usuario;
import com.borboletta.vueltaaltablero.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/* Adaptador entre nuestros usuarios y Spring Security: dado un correo, le entrega
el hash y el rol. La comparación de contraseñas la hace Spring. */
@Service
@RequiredArgsConstructor
public class AutenticacionService implements UserDetailsService{

    private final UsuarioRepository usuarioRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String correo) throws UsernameNotFoundException{
        Usuario.Credenciales credenciales = usuarioRepository
            .findByCorreoIgnoreCaseAndFechaBajaIsNull(correo)  
            .map(Usuario::credenciales)
            .orElseThrow(() -> new UsernameNotFoundException("No existe un usuario con ese correo."));
        return aUserDetails(credenciales);
    }

    private UserDetails aUserDetails(Usuario.Credenciales credenciales){
        String rol = credenciales.administrador() ? Roles.ADMINISTRADOR : Roles.USUARIO;
        return User.withUsername(credenciales.correo())
                .password(credenciales.contraseniaCodificada())
                .roles(rol)
                .build();
    }
}