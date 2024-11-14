package cl.td.g2.eventos;

import cl.td.g2.eventos.controller.CategoriaController;
import cl.td.g2.eventos.dto.CategoriaDTO;
import cl.td.g2.eventos.service.CategoriaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class CategoriaControllerTest {

    @InjectMocks
    private CategoriaController categoriaController;

    @Mock
    private CategoriaService categoriaService;

    private CategoriaDTO categoriaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        categoriaDTO = new CategoriaDTO();
        categoriaDTO.setId(1L);
        categoriaDTO.setNombre("Tecnología");
    }

    @Test
    void testGetAllCategorias() {
        List<CategoriaDTO> categoriaList = Arrays.asList(categoriaDTO);
        when(categoriaService.getAllCategorias()).thenReturn(categoriaList);

        ResponseEntity<List<CategoriaDTO>> response = categoriaController.getAllCategorias();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(categoriaDTO.getNombre(), response.getBody().get(0).getNombre());
    }

    @Test
    void testGetCategoriaById() {
        when(categoriaService.getCategoriaById(1L)).thenReturn(categoriaDTO);

        ResponseEntity<CategoriaDTO> response = categoriaController.getCategoriaById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoriaDTO.getNombre(), response.getBody().getNombre());
    }

    @Test
    void testCreateCategoria() {
        when(categoriaService.createCategoria(any(CategoriaDTO.class))).thenReturn(categoriaDTO);

        ResponseEntity<CategoriaDTO> response = categoriaController.createCategoria(categoriaDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(categoriaDTO.getNombre(), response.getBody().getNombre());
    }

    @Test
    void testUpdateCategoria() {
        when(categoriaService.updateCategoria(any(Long.class), any(CategoriaDTO.class))).thenReturn(categoriaDTO);

        ResponseEntity<CategoriaDTO> response = categoriaController.updateCategoria(1L, categoriaDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(categoriaDTO.getNombre(), response.getBody().getNombre());
    }

    @Test
    void testDeleteCategoria() {
        ResponseEntity<Void> response = categoriaController.deleteCategoria(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
