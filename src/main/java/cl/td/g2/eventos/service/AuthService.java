package cl.td.g2.eventos.service;

import org.springframework.stereotype.Service;

@Service
public class AuthService {

    // Verificar credenciales sin encriptación
    public boolean verificarCredenciales(String contraseñaIngresada, String hashAlmacenado) {
        // Comparar las contraseñas directamente sin usar passwordEncoder
        return contraseñaIngresada.equals(hashAlmacenado);
    }
}
