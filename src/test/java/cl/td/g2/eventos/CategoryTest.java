package cl.td.g2.eventos;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;

import cl.td.g2.eventos.model.Categoria;

public class CategoryTest {

    @Test
    public void testCategoryCreation() {
        Categoria category = new Categoria(1L, "Conference");

        assertNotNull(category);
        assertEquals(1L, category.getId());
        assertEquals("Conference", category.getNombre());

        // Verificar que el nombre no esté vacío
        assertFalse(category.getNombre().isEmpty());

        // Verificar que el nombre tenga una longitud válida
        assertTrue(category.getNombre().length() >= 3);
    }
}
