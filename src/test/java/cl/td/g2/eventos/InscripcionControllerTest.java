package cl.td.g2.eventos;

import cl.td.g2.eventos.controller.InscripcionController;
import cl.td.g2.eventos.dto.InscripcionDTO;
import cl.td.g2.eventos.service.InscripcionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class InscripcionControllerTest {

    @InjectMocks
    private InscripcionController inscripcionController;

    @Mock
    private InscripcionService inscripcionService;

    private InscripcionDTO inscripcionDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        inscripcionDTO = new InscripcionDTO();
        inscripcionDTO.setId(1L);
        inscripcionDTO.setEventoId(1L);
        inscripcionDTO.setUsuarioId(1L);
        inscripcionDTO.setFechaInscripcion(LocalDateTime.now());
    }

    @Test
    void testGetAllInscripciones() {
        List<InscripcionDTO> inscripcionList = Arrays.asList(inscripcionDTO);
        when(inscripcionService.getAllInscripciones()).thenReturn(inscripcionList);

        ResponseEntity<List<InscripcionDTO>> response = inscripcionController.getAllInscripciones();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(inscripcionDTO.getEventoId(), response.getBody().get(0).getEventoId());
    }

    @Test
    void testGetInscripcionById() {
        when(inscripcionService.getInscripcionById(1L)).thenReturn(inscripcionDTO);

        ResponseEntity<InscripcionDTO> response = inscripcionController.getInscripcionById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inscripcionDTO.getEventoId(), response.getBody().getEventoId());
    }

    @Test
    void testCreateInscripcion() {
        when(inscripcionService.createInscripcion(any(InscripcionDTO.class))).thenReturn(inscripcionDTO);

        ResponseEntity<InscripcionDTO> response = inscripcionController.createInscripcion(inscripcionDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(inscripcionDTO.getEventoId(), response.getBody().getEventoId());
    }

    @Test
    void testUpdateInscripcion() {
        when(inscripcionService.updateInscripcion(any(Long.class), any(InscripcionDTO.class))).thenReturn(inscripcionDTO);

        ResponseEntity<InscripcionDTO> response = inscripcionController.updateInscripcion(1L, inscripcionDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(inscripcionDTO.getEventoId(), response.getBody().getEventoId());
    }
    @Test
    void testDeleteInscripcion() {
        ResponseEntity<Void> response = inscripcionController.deleteInscripcion(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}