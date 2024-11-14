package cl.td.g2.eventos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import cl.td.g2.eventos.model.Categoria;
import cl.td.g2.eventos.model.Ciudad;
import cl.td.g2.eventos.model.Evento;
import cl.td.g2.eventos.model.Inscripcion;
import cl.td.g2.eventos.model.Usuario;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class InscripcionTest {

    @Test
    public void testInscripcionCreation() {
        // Crear las instancias de Categoria y Ciudad necesarias
        Categoria categoria = new Categoria(1L, "Conferencia");
        Ciudad ciudad = new Ciudad(1L, "Madrid");

        // Crear un nuevo evento con todos los campos necesarios
        Evento evento = new Evento("Conferencia de Tecnología", 
                                LocalDateTime.of(2024, 8, 1, 10, 0), 
                                LocalDateTime.of(2024, 8, 1, 12, 0), 
                                categoria, 
                                "Auditorio Principal", 
                                ciudad, 
                                BigDecimal.valueOf(50.0));

        Usuario usuario = new Usuario(1L, "admin", "admin@example.com", "password123");

        Inscripcion inscripcion = new Inscripcion(usuario, evento);

        assertNotNull(inscripcion);
        assertEquals(usuario, inscripcion.getUsuario());
        assertEquals(evento, inscripcion.getEvento());
        assertNotNull(inscripcion.getFechaInscripcion());

        // Verificar que la fecha de inscripción no sea nula
        assertNotNull(inscripcion.getFechaInscripcion());

        // Verificar que la fecha de inscripción sea una fecha y hora válidas
        assertTrue(inscripcion.getFechaInscripcion().isBefore(LocalDateTime.now().plusSeconds(1)));
    }
}

