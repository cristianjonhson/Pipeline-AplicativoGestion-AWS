package cl.td.g2.eventos;

import cl.td.g2.eventos.controller.CiudadController;
import cl.td.g2.eventos.dto.CiudadDTO;
import cl.td.g2.eventos.service.CiudadService;
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

class CiudadControllerTest {

    @InjectMocks
    private CiudadController ciudadController;

    @Mock
    private CiudadService ciudadService;

    private CiudadDTO ciudadDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ciudadDTO = new CiudadDTO();
        ciudadDTO.setId(1L);
        ciudadDTO.setNombre("Santiago");
    }

    @Test
    void testGetAllCiudades() {
        List<CiudadDTO> ciudadList = Arrays.asList(ciudadDTO);
        when(ciudadService.getAllCiudades()).thenReturn(ciudadList);

        ResponseEntity<List<CiudadDTO>> response = ciudadController.getAllCiudades();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(ciudadDTO.getNombre(), response.getBody().get(0).getNombre());
    }

    @Test
    void testGetCiudadById() {
        when(ciudadService.getCiudadById(1L)).thenReturn(ciudadDTO);

        ResponseEntity<CiudadDTO> response = ciudadController.getCiudadById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ciudadDTO.getNombre(), response.getBody().getNombre());
    }

    @Test
    void testCreateCiudad() {
        when(ciudadService.createCiudad(any(CiudadDTO.class))).thenReturn(ciudadDTO);

        ResponseEntity<CiudadDTO> response = ciudadController.createCiudad(ciudadDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(ciudadDTO.getNombre(), response.getBody().getNombre());
    }

    @Test
    void testUpdateCiudad() {
        when(ciudadService.updateCiudad(any(Long.class), any(CiudadDTO.class))).thenReturn(ciudadDTO);

        ResponseEntity<CiudadDTO> response = ciudadController.updateCiudad(1L, ciudadDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(ciudadDTO.getNombre(), response.getBody().getNombre());
    }

    @Test
    void testDeleteCiudad() {
        ResponseEntity<Void> response = ciudadController.deleteCiudad(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
