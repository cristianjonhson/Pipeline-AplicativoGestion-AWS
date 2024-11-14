package cl.td.g2.eventos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import cl.td.g2.eventos.model.Ciudad;

public class CiudadTest {

    @Test
    public void testCiudadCreation() {
        // Crear una nueva instancia de Ciudad
        Ciudad ciudad = new Ciudad(1L, "Madrid");

        // Verificar que la instancia se creó correctamente y que los valores son correctos
        assertNotNull(ciudad);
        assertEquals(1L, ciudad.getId());
        assertEquals("Madrid", ciudad.getNombre());
    }

    @Test
    public void testSettersAndGetters() {
        // Crear una instancia de Ciudad y modificar los valores usando los setters
        Ciudad ciudad = new Ciudad(1L, "Madrid");
        ciudad.setId(2L);
        ciudad.setNombre("Barcelona");

        // Verificar que los valores se actualizaron correctamente
        assertEquals(2L, ciudad.getId());
        assertEquals("Barcelona", ciudad.getNombre());
    }
}

