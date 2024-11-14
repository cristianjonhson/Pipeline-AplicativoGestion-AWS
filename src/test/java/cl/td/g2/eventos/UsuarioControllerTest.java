package cl.td.g2.eventos;

import cl.td.g2.eventos.controller.UsuarioController;
import cl.td.g2.eventos.dto.UsuarioDTO;
import cl.td.g2.eventos.service.UsuarioService;
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

class UsuarioControllerTest {

    @InjectMocks
    private UsuarioController usuarioController;

    @Mock
    private UsuarioService usuarioService;

    private UsuarioDTO usuarioDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioDTO = new UsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setEmail("test@example.com");
        usuarioDTO.setContrasena("password123");
        usuarioDTO.setNombre("Test");
        usuarioDTO.setApellido("User");
        usuarioDTO.setFechaRegistro(LocalDateTime.now());
        usuarioDTO.setRol("Usuario");
    }

    @Test
    void testGetAllUsuarios() {
        List<UsuarioDTO> usuarioList = Arrays.asList(usuarioDTO);
        when(usuarioService.getAllUsuarios()).thenReturn(usuarioList);

        ResponseEntity<List<UsuarioDTO>> response = usuarioController.getAllUsuarios();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals(usuarioDTO.getEmail(), response.getBody().get(0).getEmail());
    }

    @Test
    void testGetUsuarioById() {
        when(usuarioService.getUsuarioById(1L)).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.getUsuarioById(1L);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioDTO.getEmail(), response.getBody().getEmail());
    }

    @Test
    void testCreateUsuario() {
        when(usuarioService.createUsuario(any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.createUsuario(usuarioDTO);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(usuarioDTO.getEmail(), response.getBody().getEmail());
    }

    @Test
    void testUpdateUsuario() {
        when(usuarioService.updateUsuario(any(Long.class), any(UsuarioDTO.class))).thenReturn(usuarioDTO);

        ResponseEntity<UsuarioDTO> response = usuarioController.updateUsuario(1L, usuarioDTO);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(usuarioDTO.getEmail(), response.getBody().getEmail());
    }

    @Test
    void testDeleteUsuario() {
        ResponseEntity<Void> response = usuarioController.deleteUsuario(1L);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

   
}

