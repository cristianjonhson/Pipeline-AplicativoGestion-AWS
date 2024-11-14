package cl.td.g2.eventos.service;

import cl.td.g2.eventos.dto.UsuarioDTO;
import cl.td.g2.eventos.model.Usuario;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UsuarioService usuarioService;

    public CustomUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Obtener el usuario DTO por email
        UsuarioDTO usuarioDTO = usuarioService.findUsuarioByEmail(email);
        if (usuarioDTO == null) {
            throw new UsernameNotFoundException("Usuario no encontrado con el email: " + email);
        }
    
        // Si el usuarioDTO es encontrado, mapea a Usuario
        // Puedes crear un nuevo objeto Usuario (que implementa UserDetails)
        Usuario usuario = new Usuario();
        usuario.setNombre(usuarioDTO.getNombre());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setContrasena(usuarioDTO.getContrasena()); // Asegúrate de que la contraseña esté codificada
    
        // Crear una lista de GrantedAuthority
        List<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(usuarioDTO.getRol())); // Asigna el rol aquí
    
        // Crear un UserDetails con los detalles del usuario
        return User.withUsername(usuario.getEmail())
                .password(usuario.getContrasena())  // La contraseña debe estar codificada
                .authorities(authorities)  // Asignar la lista de authorities
                .accountExpired(false) // Puedes definir si la cuenta está expirada
                .accountLocked(false)  // Puedes definir si la cuenta está bloqueada
                .credentialsExpired(false) // Puedes definir si las credenciales están expiradas
                .disabled(false) // Puedes definir si la cuenta está deshabilitada
                .build();
    }
}    
