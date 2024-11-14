package cl.td.g2.eventos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import cl.td.g2.eventos.model.Usuario;


public class UserTest {

    @Test
    public void testUsuarioCreation() {
        // Crear una instancia de Usuario
        Usuario usuario = new Usuario(1L, "Juan Pérez", "juan.perez@example.com", "password123");

        // Verificar que la instancia no es nula
        assertNotNull(usuario);

        // Verificar que los valores de los atributos son los esperados
        assertEquals(1L, usuario.getId());
        assertEquals("Juan Pérez", usuario.getNombre());
        assertEquals("juan.perez@example.com", usuario.getEmail());
        assertEquals("password123", usuario.getContrasena());

        // Verificar que se pueden cambiar los valores con los setters
        usuario.setNombre("Carlos López");
        usuario.setEmail("carlos.lopez@example.com");
        usuario.setContrasena("newpassword456");

        assertEquals("Carlos López", usuario.getNombre());
        assertEquals("carlos.lopez@example.com", usuario.getEmail());
        assertEquals("newpassword456", usuario.getContrasena());
    }
}

