package cl.td.g2.eventos.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import cl.td.g2.eventos.model.Inscripcion;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface InscripcionRepository extends JpaRepository<Inscripcion, Long> {
    
    // Encontrar por ID
    Optional<Inscripcion> findById(Long id);

    // Encontrar todos
    List<Inscripcion> findAll();

    // Guardar
    @SuppressWarnings("unchecked")
	Inscripcion save(Inscripcion inscripcion);

    // Eliminar por ID
    void deleteById(Long id);
    
    // Método para encontrar por usuario y evento
    Optional<Inscripcion> findByUsuarioIdAndEventoId(Long usuarioId, Long eventoId);
    
    // Método para encontrar inscripciones por usuario
    List<Inscripcion> findByUsuarioId(Long usuarioId);

    // Método para encontrar inscripciones por evento
    List<Inscripcion> findByEventoId(Long eventoId);

    // Método para encontrar inscripciones por fecha
    List<Inscripcion> findByFechaInscripcionAfter(LocalDateTime fechaInscripcion);
}
