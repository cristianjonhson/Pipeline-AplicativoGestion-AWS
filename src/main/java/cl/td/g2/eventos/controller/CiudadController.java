package cl.td.g2.eventos.controller;

import cl.td.g2.eventos.dto.CiudadDTO;
import cl.td.g2.eventos.service.CiudadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ciudades")
@Tag(name="Servicios de Ciudad")
public class CiudadController {

    @Autowired
    private CiudadService ciudadService;

    @GetMapping
    @Operation(summary="Obtener todas las Ciudades")
    public ResponseEntity<List<CiudadDTO>> getAllCiudades() {
        return ResponseEntity.ok(ciudadService.getAllCiudades());
    }

    @GetMapping("/{id}")
    @Operation(summary="Obtener una Ciudad")
    public ResponseEntity<CiudadDTO> getCiudadById(@PathVariable Long id) {
        CiudadDTO ciudadDTO = ciudadService.getCiudadById(id);
        return ResponseEntity.ok(ciudadDTO);
    }

    @PostMapping
    @Operation(summary="Crear una Ciudad")
    public ResponseEntity<CiudadDTO> createCiudad(@Valid @RequestBody CiudadDTO ciudadDTO) {
        CiudadDTO createdCiudad = ciudadService.createCiudad(ciudadDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdCiudad);
    }

    @PutMapping("/{id}")
    @Operation(summary="Actualizar una Ciudad")
    public ResponseEntity<CiudadDTO> updateCiudad(@PathVariable Long id, @Valid @RequestBody CiudadDTO ciudadDTO) {
        CiudadDTO updatedCiudad = ciudadService.updateCiudad(id, ciudadDTO);
        return ResponseEntity.ok(updatedCiudad);
    }

    @DeleteMapping("/{id}")
    @Operation(summary="Eliminar una Ciudad")
    public ResponseEntity<Void> deleteCiudad(@PathVariable Long id) {
        ciudadService.deleteCiudad(id);
        return ResponseEntity.noContent().build();
    }
}
