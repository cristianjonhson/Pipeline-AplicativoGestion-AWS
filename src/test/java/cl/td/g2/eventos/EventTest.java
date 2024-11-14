package cl.td.g2.eventos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import cl.td.g2.eventos.model.Categoria;
import cl.td.g2.eventos.model.Ciudad;
import cl.td.g2.eventos.model.Evento;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class EventTest {

    @Test
    public void testEventCreation() {
        // Crear un nuevo evento con todos los campos necesarios
       // Crear las instancias de Categoria y Ciudad necesarias
        Categoria categoria = new Categoria(1L, "Conferencia");
        Ciudad ciudad = new Ciudad(1L, "Madrid");

        // Crear un nuevo evento con todos los campos necesarios
        Evento event = new Evento("Conferencia de Tecnología", 
                                LocalDateTime.of(2024, 8, 1, 10, 0), 
                                LocalDateTime.of(2024, 8, 1, 12, 0), 
                                categoria, 
                                "Auditorio Principal", 
                                ciudad, 
                                BigDecimal.valueOf(50.0));

        // Verificar que el objeto evento no sea nulo
        assertNotNull(event);

        // Verificar que el nombre del evento sea el esperado
        assertEquals("Conferencia de Tecnología", event.getTitulo());

        // Verificar que la fecha y hora de inicio sea la esperada
        assertEquals(LocalDateTime.of(2024, 8, 1, 10, 0), event.getFechaInicio());

        // Verificar que la fecha y hora de fin sea la esperada
        assertEquals(LocalDateTime.of(2024, 8, 1, 12, 0), event.getFechaFin());

        // Verificar que la categoría del evento sea la esperada
        assertEquals("Conferencia", event.getCategoria().getNombre());

        // Verificar que la ubicación del evento sea la esperada
        assertEquals("Auditorio Principal", event.getUbicacion());

        // Verificar que la ciudad del evento sea la esperada
        assertEquals("Madrid", event.getCiudad().getNombre());

        // Verificar que el valor del evento sea el esperado
        assertEquals(BigDecimal.valueOf(50.0), event.getValor());

        // Verificar que las fechas y horas de inicio y fin no sean iguales
        assertNotEquals(event.getFechaInicio(), event.getFechaFin());
    }
}
