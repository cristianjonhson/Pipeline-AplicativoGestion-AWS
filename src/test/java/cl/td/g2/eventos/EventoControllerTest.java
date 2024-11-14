package cl.td.g2.eventos;

import cl.td.g2.eventos.controller.EventoController;
import cl.td.g2.eventos.dto.EventoDTO;
import cl.td.g2.eventos.service.EventoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class EventoControllerTest {

    @InjectMocks
    private EventoController eventoController;

    @Mock
    private EventoService eventoService;

    private EventoDTO eventoDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        eventoDTO = new EventoDTO();
        eventoDTO.setId(1L);
        eventoDTO.setTitulo("Evento 1");
        eventoDTO.setDescripcion("Descripción del evento 1");
        eventoDTO.setFechaInicio(LocalDateTime.of(2024, 9, 10, 10, 0));
        eventoDTO.setFechaFin(LocalDateTime.of(2024, 9, 10, 12, 0));
        eventoDTO.setUbicacion("Ubicación 1");
        eventoDTO.setOrganizadorId(1L);
        eventoDTO.setCategoriaId(1L);
        eventoDTO.setCiudadId(1L);
        eventoDTO.setValor(BigDecimal.valueOf(100.00));
        eventoDTO.setImagenHtml("<img src='imagen1.jpg'/>");
        eventoDTO.setFechaCreacion(LocalDateTime.now());
    }

    @Test
    void testGetAllEventos() {
        List<EventoDTO> eventoList = Arrays.asList(eventoDTO);
        when(eventoService.getAllEventos()).thenReturn(eventoList);

        ResponseEntity<List<EventoDTO>> response = eventoController.getAllEventos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(eventoDTO.getTitulo(), response.getBody().get(0).getTitulo());
    }

    @Test
    void testGetEventoById() {
        when(eventoService.getEventoById(1L)).thenReturn(eventoDTO);

        ResponseEntity<EventoDTO> response = eventoController.getEventoById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventoDTO.getTitulo(), response.getBody().getTitulo());
    }

    @Test
    void testCreateEvento() {
        when(eventoService.createEvento(any(EventoDTO.class))).thenReturn(eventoDTO);

        ResponseEntity<EventoDTO> response = eventoController.createEvento(eventoDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(eventoDTO.getTitulo(), response.getBody().getTitulo());
    }

    @Test
    void testUpdateEvento() {
        when(eventoService.updateEvento(any(Long.class), any(EventoDTO.class))).thenReturn(eventoDTO);

        ResponseEntity<EventoDTO> response = eventoController.updateEvento(1L, eventoDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(eventoDTO.getTitulo(), response.getBody().getTitulo());
    }

    @Test
    void testDeleteEvento() {
        ResponseEntity<Void> response = eventoController.deleteEvento(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }
}
