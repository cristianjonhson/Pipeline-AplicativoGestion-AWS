package cl.td.g2.eventos.controller;

import cl.td.g2.eventos.dto.InscripcionDTO;
import cl.td.g2.eventos.service.InscripcionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/inscripciones")
@Tag(name="Servicios de Inscripción")
public class InscripcionController {

    @Autowired
    private InscripcionService inscripcionService;

    @GetMapping
    @Operation(summary="Obtener todas las Inscripciones")
    public ResponseEntity<List<InscripcionDTO>> getAllInscripciones() {
        return ResponseEntity.ok(inscripcionService.getAllInscripciones());
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener una Inscripción")
    public ResponseEntity<InscripcionDTO> getInscripcionById(@PathVariable Long id) {
        InscripcionDTO inscripcionDTO = inscripcionService.getInscripcionById(id);
        return ResponseEntity.ok(inscripcionDTO);
    }

    @PostMapping
    @Operation(summary="Crear una Inscripción")
    public ResponseEntity<InscripcionDTO> createInscripcion(@Valid @RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO createdInscripcion = inscripcionService.createInscripcion(inscripcionDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdInscripcion);
    }

    @PutMapping("/{id}")
    @Operation(summary="Actualizar una Inscripción")
    public ResponseEntity<InscripcionDTO> updateInscripcion(@PathVariable Long id, @Valid @RequestBody InscripcionDTO inscripcionDTO) {
        InscripcionDTO updatedInscripcion = inscripcionService.updateInscripcion(id, inscripcionDTO);
        return ResponseEntity.ok(updatedInscripcion);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar una Inscripción")
    public ResponseEntity<Void> deleteInscripcion(@PathVariable Long id) {
        inscripcionService.deleteInscripcion(id);
        return ResponseEntity.noContent().build();
    }
}
